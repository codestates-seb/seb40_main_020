package OneCoin.Server.config;

import OneCoin.Server.config.auth.filter.JwtAuthenticationFilter;
import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenizer jwtTokenizer;

    public SecurityConfig(JwtTokenizer jwtTokenizer) {
        this.jwtTokenizer = jwtTokenizer;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin() // 동일 출처로부터 들어오는 request 만 페이지 렌더링을 허용
                .and()
                .csrf().disable()        // 일단 disable
                .cors(Customizer.withDefaults())    // corsConfigurationSource 에서 설정
                .formLogin().disable()
                .httpBasic().disable()   // jwt 쓸거니 비활성화
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()                // 일단 허용
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Cors 정책 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));   // 모든 origin 허용
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "DELETE"));  // 허용 메소드 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();   // CorsConfigurationSource 구현 클래스
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // JwtAuthenticationFilter 를 등록
    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);  // JwtAuthenticationFilter 를 생성하면서 JwtAuthenticationFilter 에서 사용되는 AuthenticationManager 와 JwtTokenizer 를 DI
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");          // 로그인 url 변경

            builder.addFilter(jwtAuthenticationFilter);  // 커스텀한 필터를 필터 체인에 추가
        }
    }
}
