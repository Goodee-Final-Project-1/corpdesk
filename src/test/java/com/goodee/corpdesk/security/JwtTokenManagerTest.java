package com.goodee.corpdesk.security;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.security.token.RefreshToken;
import com.goodee.corpdesk.security.token.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for JwtTokenManager.
 *
 * Uses JUnit 5 (Jupiter) and Mockito.
 */
class JwtTokenManagerTest {

    private JwtTokenManager jwtTokenManager;

    private EmployeeRepository employeeRepository;
    private RefreshTokenRepository refreshTokenRepository;

    private final String issuer = "test-issuer";
    private final String secret = "this-is-a-very-long-test-secret-key-which-is-at-least-64-bytes-long-0123456789";
    private final long accessValidMs = 10_000L;     // 10s
    private final long refreshValidMs = 3_600_000L; // 1h

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenManager = new JwtTokenManager();

        employeeRepository = mock(EmployeeRepository.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);

        // Inject @Value and @Autowired fields via reflection
        setField(jwtTokenManager, "secretKey", secret);
        setField(jwtTokenManager, "accessValidTime", accessValidMs);
        setField(jwtTokenManager, "refreshValidTime", refreshValidMs);
        setField(jwtTokenManager, "issuer", issuer);
        setField(jwtTokenManager, "employeeRepository", employeeRepository);
        setField(jwtTokenManager, "refreshTokenRepository", refreshTokenRepository);

        // Initialize signing key
        jwtTokenManager.init();
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    private static Object getField(Object target, String fieldName) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(target);
    }

    private Authentication authFor(String username) {
        return new UsernamePasswordAuthenticationToken(
                username,
                "pw",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    private Employee mockEmployeeUserDetails(String username) {
        Employee employee = mock(Employee.class);
        when(employee.getUsername()).thenReturn(username);
        when(employee.getAuthorities()).thenReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        return employee;
    }

    @Test
    @DisplayName("makeAccessToken: produces signed JWT with subject/issuer and expected TTL")
    void makeAccessToken_generatesSignedTokenWithExpectedClaims() throws Exception {
        String username = "alice";
        Authentication authentication = authFor(username);
        when(employeeRepository.findById(username)).thenReturn(Optional.of(mockEmployeeUserDetails(username)));

        String token = jwtTokenManager.makeAccessToken(authentication);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        SecretKey key = (SecretKey) getField(jwtTokenManager, "key");
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        assertEquals(username, claims.getSubject());
        assertEquals(issuer, claims.getIssuer());

        long ttlFromClaims = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
        assertTrue(ttlFromClaims <= accessValidMs + 1000 && ttlFromClaims >= accessValidMs - 1000,
                "Access token TTL should be close to configured value");
    }

    @Test
    @DisplayName("makeRefreshToken: saves refresh token with username and returns same token; TTL matches refreshValidTime")
    void makeRefreshToken_savesRefreshTokenAndReturnsBody() throws Exception {
        String username = "bob";
        Authentication authentication = authFor(username);
        when(employeeRepository.findById(username)).thenReturn(Optional.of(mockEmployeeUserDetails(username)));

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        when(refreshTokenRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        String token = jwtTokenManager.makeRefreshToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        RefreshToken saved = captor.getValue();
        assertNotNull(saved);
        assertEquals(username, saved.getUsername());
        assertEquals(token, saved.getBody());

        SecretKey key = (SecretKey) getField(jwtTokenManager, "key");
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        long ttlFromClaims = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
        assertTrue(ttlFromClaims <= refreshValidMs + 1000 && ttlFromClaims >= refreshValidMs - 1000,
                "Refresh token TTL should be close to configured value");
        assertTrue(ttlFromClaims > accessValidMs, "Refresh token TTL should be greater than access token TTL");
    }

    @Test
    @DisplayName("makeToken: throws when employee not found")
    void makeToken_throwsWhenEmployeeMissing() {
        String username = "missing";
        Authentication authentication = authFor(username);
        when(employeeRepository.findById(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> jwtTokenManager.makeToken(authentication, accessValidMs));
    }

    @Test
    @DisplayName("getAuthentication: parses token and returns Authentication with expected principal and authorities")
    void getAuthentication_returnsExpectedAuthentication() throws Exception {
        String username = "carol";
        Authentication authentication = authFor(username);

        Employee employeeDetails = mockEmployeeUserDetails(username);
        when(employeeRepository.findById(username)).thenReturn(Optional.of(employeeDetails));

        String token = jwtTokenManager.makeAccessToken(authentication);

        Authentication parsed = jwtTokenManager.getAuthentication(token);

        assertNotNull(parsed);
        assertEquals(username, parsed.getName());
        assertEquals(1, parsed.getAuthorities().size());
        assertTrue(parsed.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertSame(employeeDetails, parsed.getPrincipal());
    }

    @Test
    @DisplayName("getAuthentication: throws for invalid signature and does not hit repositories")
    void getAuthentication_throwsForInvalidSignature() throws Exception {
        byte[] otherKeyBytes = Base64.getEncoder().encode("other-secret-which-is-also-long-enough-0123456789".getBytes(StandardCharsets.UTF_8));
        SecretKey otherKey = Keys.hmacShaKeyFor(otherKeyBytes);

        String username = "dave";
        String forged = Jwts.builder()
                .subject(username)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessValidMs))
                .signWith(otherKey)
                .compact();

        assertThrows(Exception.class, () -> jwtTokenManager.getAuthentication(forged));
        verifyNoInteractions(employeeRepository, refreshTokenRepository);
    }

    @Nested
    @DisplayName("getRefreshToken using ExpiredJwtException")
    class GetRefreshTokenTests {

        @Test
        @DisplayName("returns latest refresh token for subject from expired access token")
        void returnsLatestRefreshTokenForExpiredAccessToken() throws Exception {
            String username = "eve";

            SecretKey key = (SecretKey) getField(jwtTokenManager, "key");
            String expiredToken = Jwts.builder()
                    .subject(username)
                    .issuer(issuer)
                    .issuedAt(new Date(System.currentTimeMillis() - 10_000))
                    .expiration(new Date(System.currentTimeMillis() - 5_000))
                    .signWith(key)
                    .compact();

            ExpiredJwtException expiredEx = null;
            try {
                Jwts.parser().verifyWith(key).build().parseSignedClaims(expiredToken);
                fail("Parsing should throw ExpiredJwtException");
            } catch (ExpiredJwtException e) {
                expiredEx = e;
            }
            assertNotNull(expiredEx);

            RefreshToken latest = new RefreshToken();
            latest.setUsername(username);
            latest.setBody("saved-refresh-token-body");
            when(refreshTokenRepository.findTopByUsernameOrderByTokenIdDesc(username)).thenReturn(Optional.of(latest));

            RefreshToken resolved = jwtTokenManager.getRefreshToken(expiredEx);

            assertNotNull(resolved);
            assertEquals(username, resolved.getUsername());
            assertEquals("saved-refresh-token-body", resolved.getBody());
        }

        @Test
        @DisplayName("throws when no refresh token found for subject in repository")
        void throwsWhenNoRefreshTokenFound() throws Exception {
            String username = "frank";
            SecretKey key = (SecretKey) getField(jwtTokenManager, "key");
            String expiredToken = Jwts.builder()
                    .subject(username)
                    .issuer(issuer)
                    .issuedAt(new Date(System.currentTimeMillis() - 10_000))
                    .expiration(new Date(System.currentTimeMillis() - 5_000))
                    .signWith(key)
                    .compact();

            ExpiredJwtException expiredEx = null;
            try {
                Jwts.parser().verifyWith(key).build().parseSignedClaims(expiredToken);
                fail("Parsing should throw ExpiredJwtException");
            } catch (ExpiredJwtException e) {
                expiredEx = e;
            }
            assertNotNull(expiredEx);

            when(refreshTokenRepository.findTopByUsernameOrderByTokenIdDesc(username)).thenReturn(Optional.empty());

            assertThrows(NoSuchElementException.class, () -> jwtTokenManager.getRefreshToken(expiredEx));
        }
    }

    @Test
    @DisplayName("getRefreshValidTime: returns seconds as integer")
    void getRefreshValidTime_returnsSeconds() {
        int seconds = jwtTokenManager.getRefreshValidTime();
        assertEquals((int) (refreshValidMs / 1000), seconds);
    }
}