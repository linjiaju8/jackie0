<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>JSP视图测试</title>
</head>
<body>
<h1>JSP视图测试</h1>

<p>Hello World!</p>
<c:out value="c:out标签测试"/>

<div>
    <h3>spring-boot启用jsp视图展示说明</h3>
    <p>因为spring-boot默认使用jar+内嵌web容器方式启动web应用，而内嵌的tomcat及jetty精简了许多内容，对JSP的支持不佳，所以spring-boot默认支持以JSP作为视图展示。</p>
    <p>如要做如下的操作才能启用：</p>
    <textarea readonly cols="150" rows="42">
        1、application.yml（application.properties本项目都以yml为例）中配置：
        mvc:
        view:
        prefix: /WEB-INF/views/
        suffix: .jsp
        注意如果要同时支持多视图suffix可配置为空

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

        4、配置maven项目打包方式为war包，不要使用main方法启动应用，一定要放到tomcat或其他web服务器跑，即像没有使用spring-boot时在ide配置tomcat来跑应用。
    </textarea>
</div>
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