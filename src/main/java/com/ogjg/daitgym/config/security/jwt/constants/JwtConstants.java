package com.ogjg.daitgym.config.security.jwt.constants;

import org.springframework.http.HttpHeaders;

public class JwtConstants {
    public static final String CHARSET_UTF_8 = "UTF-8";

    public static class HEADER {
        public static class KEY {
            public static final String ALGORITHM = "alg";
            public static final String TYPE = "typ";
            public static final String TOKEN_TYPE = "token_type";
        }

        public static class VALUE {
            public static final String ALGORITHM_HS256 = "HS256";
            public static final String TYPE_JWT = "JWT";
        }
    }

    public static class CLAIM {
        public static class KEY {
            public static final String ISSUER = "iss";
        }

        public static class VALUE {
            public static final String ISSUER = "team_ogjg";
        }
    }

    public static class ACCESS_TOKEN {
        public static final String PREFIX = "Bearer ";
        public static final String HTTP_HEADER_AUTHORIZATION = HttpHeaders.AUTHORIZATION;
        public static final int VALID_TIME = MINUTE * 5;
        public static final String TOKEN_TYPE = "access";
    }

    public static class REFRESH_TOKEN {
        public static final long VALID_TIME = DAY * 30L;
        public static final long MAX_AGE = VALID_TIME / 1000;
        public static final String TOKEN_TYPE = "refresh";

        public static class COOKIE {
            public static final String NAME = "refreshToken";
        }
    }

    public static final int MINUTE = 60 * 1000;
    public static final int HOUR = MINUTE * 60;
    public static final int DAY = HOUR * 24;
}
