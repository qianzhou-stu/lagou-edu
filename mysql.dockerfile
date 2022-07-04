FROM mysql:5.7.31
MAINTAINER mysql from date UTC By Asia/Shanghai "1292184670@qq.com"
ENV TZ Asiz/Shanghai
COPY edu_ad.sql /docker-entrypoint-initdb.d
COPY edu_authority.sql /docker-entrypoint-initdb.d
COPY edu_comment.sql /docker-entrypoint-initdb.d
COPY edu_course.sql /docker-entrypoint-initdb.d

COPY edu_oauth.sql /docker-entrypoint-initdb.d
COPY edu_order.sql /docker-entrypoint-initdb.d
COPY edu_pay.sql /docker-entrypoint-initdb.d
COPY edu_user.sql /docker-entrypoint-initdb.d