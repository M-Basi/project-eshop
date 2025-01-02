package gr.eshop.marios.EshopApp.security;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);



    private String secretKey = "5ce98d378ec88ea09ba8bcd511ef23645f04cc8e70b9134b98723a53c275bbc5";
    private long jwtExpiration = 10_800_000;  // 3 hours in milliseconds



    /**
     * Generates a JWT token for a given username and role.
     *
     * @param username the username for which the token is generated
     * @param role the role to be included as a claim in the token
     * @return the generated JWT token
     */
    public String generateToken(String username, String role) {
        var claims = new HashMap<String, Object>();
        claims.put("role", role);
        String token = Jwts
                .builder()
                .setIssuer("self") // todo
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        LOGGER.info("Generated token: {}, for username: {}, role: {}", token, username, role);

        return token;
    }

    /**
     * Validates a given JWT token against the provided user details.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to match against the token
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String subject = extractSubject(token);
        final String role = getStringClaim(token, "role");
        return (subject.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    /**
     * Retrieves a specific claim from the JWT token as a string.
     *
     * @param token the JWT token
     * @param claim the name of the claim to retrieve
     * @return the value of the claim as a string
     */
    public String getStringClaim(String token, String claim) {
        return extractAllClaims(token).get(claim, String.class);
    }

    /**
     * Extracts the subject (username) from the JWT token.
     *
     * @param token the JWT token
     * @return the subject (username) of the token
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using a custom resolver function.
     *
     * @param <T> the type of the claim value
     * @param token the JWT token
     * @param claimsResolver a function to resolve the claim from the Claims object
     * @return the resolved claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Checks whether a JWT token has expired.
     *
     * @param token the JWT token
     * @return true if the token has expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the Claims object containing all claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Creates a HS256 Key. Key is an interface.
     * Starting from secretKey we get a byte array
     * of the secret. Then we get the {@link javax.crypto.SecretKey,
     * class that implements the {@link Key } interface.
     *
     *
     * @return  a SecretKey which implements Key.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}