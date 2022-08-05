/***
 * 该commons工程为提取所有module工程共用组件(或工具类代码)，目的在于避免多个工程中出现大量重复代码，提供代码复用率；
 *
 * 在其他工程中引用该工程的方法是在其pom.xml中添加依赖：
 * <dependency>
 * <groupId>com.cloud</groupId>
 * <artifactId>commonsmng</artifactId>
 * <version>0.0.1-SNAPSHOT</version>
 * </dependency>
 *
 * 引用后的工程在打包时，该commons工程是以commons-0.0.1-SNAPSHOT.jar的形式存在于包里的lib中；
 */
package com.cloud.commonsmng;