<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<body>
<h1>test</h1>

<p>Hello World!</p>
<c:out value="JSTL"/>
</body>
<!--
 spring-boot默认已经不再支持jsp视图展示，要支持jsp需要做一下工作：
 1、application.yml中配置：
   mvc:
    view:
      prefix: /WEB-INF/views/
      suffix: .jsp

 2、Application的服务启动类如下：
@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CommonApplication.class, PlatformApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication springApplication = new SpringApplication(CommonApplication.class, PlatformApplication.class);
        springApplication.run(args);
    }

 3、引入jsp解析及jstl依赖，pom.xml：
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
</dependency>

 4、配置maven项目打包方式为war包，不要使用main方法启动应用，一定要放到tomcat容器跑，即像没有使用spring-boot时在ide配置tomcat来跑应用。
 -->
</html>