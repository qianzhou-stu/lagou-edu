#!/bin/bash
echo '创建根项目edu-bom目录'
cd /data
mkdir -p edu-project
cd edu-project
echo '创建每个子项目目录'
mkdir -p mysql edu-eureka-boot edu-config-boot edu-gateway-boot edu-user-boot edu-front-boot edu-boss-boot edu-ad-boot