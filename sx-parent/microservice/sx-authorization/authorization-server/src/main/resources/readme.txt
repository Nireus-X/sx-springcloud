非对称加密
生成私钥
keytool命令生成JKS（Java KeyStore）文件
keytool -genkeypair -alias oauth2 -keyalg RSA -keypass oauth2 -keystore J:\tools\jks\oauth2.jks -storepass oauth2
导出秘钥
keytool -importkeystore -srckeystore J:\tools\jks\oauth2.jks -destkeystore J:\tools\jks\import\oauth2.jks -deststoretype pkcs12

生成公钥
keytool -list -rfc -keystore J:\tools\jks\import\oauth2.jks -storepass oauth2