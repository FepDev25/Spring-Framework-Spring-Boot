# Creating a Spring Project Without Spring Boot

## Overview

While Spring Boot simplifies application development with auto-configuration and opinionated defaults, understanding the core Spring Framework is essential for mastering how Spring works under the hood. This guide demonstrates how to create a Spring application from scratch using only the Spring Framework core libraries, focusing on the fundamental concepts of ApplicationContext and object creation.

**Key Learning Objectives:**

- Understanding the ApplicationContext and its role
- Configuring Spring using XML-based configuration
- Managing bean creation and lifecycle
- Working with the Spring IoC container manually

## Why Start Without Spring Boot?

Starting with pure Spring Framework helps you:

1. **Understand Core Concepts**: Learn how Spring's IoC container actually works
2. **Appreciate Spring Boot**: Recognize what Spring Boot automates for you
3. **Debug Better**: Know what's happening behind the scenes when issues arise
4. **Legacy Code**: Be able to work with existing Spring applications that don't use Spring Boot
5. **Fine-Grained Control**: Understand when and how to override Spring Boot's defaults

## Project Setup

### Step 1: Create a Maven Project

Create a new Maven project with the following structure:

```text
p02_spring/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── cultodeportivo/
    │   │           ├── Alien.java
    │   │           └── App.java
    │   └── resources/
    │       └── spring.xml
    └── test/
        └── java/
            └── com/
                └── cultodeportivo/
                    └── AppTest.java
```

### Step 2: Add Spring Context Dependency

The `spring-context` dependency provides the core Spring Framework functionality, including the ApplicationContext and dependency injection capabilities.

**pom.xml:**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.cultodeportivo</groupId>
    <artifactId>p02_spring</artifactId>
    <version>1</version>
    <packaging>jar</packaging>

    <name>p02_spring</name>
    <url>http://maven.apache.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Spring Context - Core Spring Framework -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>7.0.0-M9</version>
        </dependency>

        <!-- JUnit for testing -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

**What spring-context includes:**

- **spring-core**: Core utilities and classes
- **spring-beans**: Bean factory and dependency injection
- **spring-context**: ApplicationContext implementation
- **spring-expression**: Spring Expression Language (SpEL)
- **spring-aop**: Aspect-Oriented Programming support

## ApplicationContext

### What is ApplicationContext?

The **ApplicationContext** is the central interface in a Spring application for providing configuration information to the application. It represents the Spring IoC container and is responsible for instantiating, configuring, and assembling beans.

**Key Responsibilities:**

1. **Bean Factory**: Creates and manages application objects (beans)
2. **Dependency Injection**: Wires dependencies between beans
3. **Lifecycle Management**: Manages the complete lifecycle of beans
4. **Resource Loading**: Loads file resources in a generic fashion
5. **Event Propagation**: Publishes events to registered listeners
6. **Internationalization**: Supports message resolution for i18n
7. **Application-Layer Specific Context**: Provides features like automatic BeanPostProcessor registration

### ApplicationContext Hierarchy

```text
BeanFactory (interface)
    ↓
ApplicationContext (interface)
    ↓
ConfigurableApplicationContext (interface)
    ↓
AbstractApplicationContext (abstract class)
    ↓
ClassPathXmlApplicationContext (concrete implementation)
```

### Types of ApplicationContext Implementations

1. **ClassPathXmlApplicationContext**: Loads XML configuration from the classpath
2. **FileSystemXmlApplicationContext**: Loads XML configuration from the file system
3. **AnnotationConfigApplicationContext**: Loads Java-based configuration
4. **WebApplicationContext**: Specialized for web applications

## XML Configuration

### Creating the Spring Configuration File

Spring uses XML configuration files to define beans and their relationships. This file must be placed in `src/main/resources/` so Maven can copy it to the classpath.

**spring.xml:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans 
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- Bean definition for Alien class -->
    <bean id="alien" class="com.cultodeportivo.Alien" />

</beans>
```

### Understanding XML Configuration Elements

**XML Declaration:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
```

- Declares the XML version and character encoding
- Required for all XML files

**Beans Root Element:**

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="...">
```

- **xmlns**: Default namespace for Spring beans
- **xmlns:xsi**: XML Schema instance namespace
- **xsi:schemaLocation**: Specifies the location of the schema definition

**Bean Definition:**

```xml
<bean id="alien" class="com.cultodeportivo.Alien" />
```

- **id**: Unique identifier for the bean (used to retrieve it from the container)
- **class**: Fully qualified class name (including package)

### Why XML Configuration?

While modern Spring applications prefer Java-based or annotation-based configuration, XML configuration:

- **Explicit**: All configuration is visible in one place
- **Separation of Concerns**: Configuration is separate from code
- **Educational**: Helps understand Spring's bean definition mechanism
- **Legacy Support**: Many existing applications still use XML
- **External Configuration**: Can be modified without recompiling code

## Creating Application Components

### The Bean Class (Alien.java)

Create a simple Java class that will be managed by Spring:

```java
package com.cultodeportivo;

public class Alien {

    // Constructor with logging to observe when object is created
    public Alien() {
        System.out.println("Alien constructor called");
    }

    // Business method
    public void code() {
        System.out.println("coding...");
    }
}
```

**Key Points:**

- **No Spring Annotations**: This is a plain Java class (POJO)
- **Public Constructor**: Spring uses reflection to instantiate the class
- **Constructor Logging**: Helps us observe when Spring creates the object
- **Business Logic**: The `code()` method represents actual functionality

### The Application Class (App.java)

Create the main application class that bootstraps the Spring container:

```java
package com.cultodeportivo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        // 1. Initialize the Spring IoC container
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        // 2. Retrieve the bean from the container
        Alien a = (Alien) context.getBean("alien");
        
        // 3. Use the bean
        a.code();
    }
}
```

**Code Breakdown:**

1. **Container Initialization**:

   ```java
   ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
   ```

   - Creates a new ApplicationContext
   - Loads configuration from `spring.xml` on the classpath
   - Instantiates all beans defined in the configuration

2. **Bean Retrieval**:

   ```java
   Alien a = (Alien) context.getBean("alien");
   ```

   - Retrieves the bean with id "alien" from the container
   - Returns `Object`, so casting to `Alien` is required
   - The bean is already instantiated and ready to use

3. **Bean Usage**:

   ```java
   a.code();
   ```

   - Calls the business method on the bean
   - The bean is fully initialized with all dependencies (if any)

### Running the Application

When you run the application, the output will be:

```text
Alien constructor called
coding...
```

**Execution Flow:**

1. JVM starts and executes `main()` method
2. `ClassPathXmlApplicationContext` is instantiated
3. Spring reads and parses `spring.xml`
4. Spring discovers the bean definition for `alien`
5. Spring instantiates `Alien` class (constructor called)
6. Bean is stored in the container with id "alien"
7. `getBean("alien")` retrieves the already-created instance
8. `code()` method is invoked on the bean

## Object Creation in Spring

### When Are Objects Created?

**Critical Understanding**: Objects are created during the ApplicationContext initialization, not when you call `getBean()`.

```java
ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
// ↑ Objects are created HERE!
```

**What happens during this line:**

1. **Configuration Loading**: Spring reads the XML configuration file
2. **Bean Definition Registration**: Parses `<bean>` tags and registers bean definitions
3. **Bean Instantiation**: Creates instances of all singleton beans (default scope)
4. **Dependency Injection**: Injects dependencies between beans
5. **Initialization Callbacks**: Calls initialization methods if defined
6. **Container Ready**: ApplicationContext is fully initialized

### Proof: Constructor Execution Timing

Add logging to observe when objects are created:

```java
public class Alien {
    public Alien() {
        System.out.println("Alien constructor called");
    }
    
    public void code() {
        System.out.println("coding...");
    }
}
```

**Application code:**

```java
public static void main(String[] args) {
    System.out.println("Before creating ApplicationContext");
    
    ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
    
    System.out.println("After creating ApplicationContext");
    System.out.println("Before calling getBean()");
    
    Alien a = (Alien) context.getBean("alien");
    
    System.out.println("After calling getBean()");
    
    a.code();
}
```

**Output:**

```text
Before creating ApplicationContext
Alien constructor called
After creating ApplicationContext
Before calling getBean()
After calling getBean()
coding...
```

**Analysis**: The "Alien constructor called" message appears immediately after creating the ApplicationContext, proving that object creation happens during container initialization, not during `getBean()` retrieval.

### Retrieving Beans with getBean()

The `getBean()` method retrieves an already-instantiated bean from the container:

```java
Alien a = (Alien) context.getBean("alien");
a.code();
```

**What getBean() Does:**

- **Lookup**: Searches the container for a bean with the specified id
- **Return**: Returns a reference to the existing bean instance
- **No Creation**: Does NOT create a new instance (for singleton scope)
- **Type Casting**: Returns `Object`, requiring explicit casting

### Multiple Bean Definitions

You can define the same class multiple times with different bean ids:

**spring.xml:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans 
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="alien1" class="com.cultodeportivo.Alien" />
    <bean id="alien2" class="com.cultodeportivo.Alien" />

</beans>
```

**Application code:**

```java
public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
    
    Alien a1 = (Alien) context.getBean("alien1");
    Alien a2 = (Alien) context.getBean("alien2");
    
    System.out.println("a1 == a2: " + (a1 == a2));
    
    a1.code();
    a2.code();
}
```

**Output:**

```text
Alien constructor called
Alien constructor called
a1 == a2: false
coding...
coding...
```

**Key Observations:**

1. **Two Instances Created**: Constructor is called twice during container initialization
2. **Different Objects**: `a1 == a2` is `false`, meaning they are different instances
3. **Independent Beans**: Each bean definition creates a separate object
4. **Same Class**: Both beans are instances of the same `Alien` class

**Use Case**: This is useful when you need multiple instances of the same class with different configurations or initialization.

## Understanding Bean IDs

### Bean ID Rules

- **Unique**: Each bean id must be unique within the container
- **Case-Sensitive**: "alien" and "Alien" are different ids
- **Naming Convention**: Use camelCase (e.g., "userService", "dataSource")
- **Descriptive**: Should describe the bean's purpose or role

### Retrieving Beans by ID

```java
// Retrieve by id, requires casting
Alien a = (Alien) context.getBean("alien");

// Retrieve by id and type (type-safe, no casting needed)
Alien a = context.getBean("alien", Alien.class);

// Retrieve by type only (works if only one bean of this type exists)
Alien a = context.getBean(Alien.class);
```

**Recommendation**: Use type-safe methods when possible to avoid `ClassCastException`.

## ApplicationContext vs BeanFactory

### BeanFactory (Legacy)

- **Lightweight**: Basic IoC container
- **Lazy Initialization**: Beans created only when requested
- **Manual Configuration**: Requires manual setup of most features
- **Limited Features**: No automatic BeanPostProcessor registration

### ApplicationContext (Modern)

- **Feature-Rich**: Enhanced IoC container
- **Eager Initialization**: Singleton beans created at startup
- **Automatic Features**: BeanPostProcessor registration, event propagation
- **Preferred**: Recommended for all applications

**When ApplicationContext Creates Beans:**

```java
ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
// All singleton beans are created immediately
```

**When BeanFactory Creates Beans:**

```java
BeanFactory factory = new XmlBeanFactory(new ClassPathResource("spring.xml"));
// Beans are created only when getBean() is called
```

**Recommendation**: Always use ApplicationContext unless you have specific memory constraints.

## Project Structure Best Practices

### Correct Resource Location

```text
src/main/resources/spring.xml  ✓ Correct
src/main/java/resources/spring.xml  ✗ Wrong
```

**Why `src/main/resources`?**

- Maven automatically includes this directory in the classpath
- Files are copied to `target/classes/` during build
- `ClassPathXmlApplicationContext` can locate the file
- Standard Maven convention for configuration files

### Classpath Resolution

When you use:

```java
new ClassPathXmlApplicationContext("spring.xml")
```

Spring looks for the file in:

1. `target/classes/spring.xml` (compiled classpath)
2. JAR files in the classpath
3. Maven copies `src/main/resources/spring.xml` to `target/classes/spring.xml`

## Common Issues and Solutions

### Issue 1: FileNotFoundException

**Error:**

```text
java.io.FileNotFoundException: class path resource [spring.xml] cannot be opened 
because it does not exist
```

**Causes:**

- File is in wrong location (e.g., `src/main/java/resources/`)
- File name mismatch (case-sensitive)
- Maven didn't copy the file (try `mvn clean package`)

**Solution:**

- Place file in `src/main/resources/`
- Verify file name matches exactly (including extension)
- Run `mvn clean package` to rebuild

### Issue 2: BeanDefinitionStoreException

**Error:**

```text
org.springframework.beans.factory.BeanDefinitionStoreException: 
IOException parsing XML document
```

**Causes:**

- Malformed XML syntax
- Incorrect XML schema definition
- Extra closing tags

**Solution:**

- Validate XML syntax
- Check for matching opening/closing tags
- Verify schema location URLs

### Issue 3: NoSuchBeanDefinitionException

**Error:**

```text
NoSuchBeanDefinitionException: No bean named 'aliennn' available
```

**Causes:**

- Bean id mismatch
- Bean not defined in configuration
- Typo in bean id

**Solution:**

- Verify bean id in XML matches `getBean()` parameter
- Check XML configuration is loaded correctly
- Use type-safe `getBean(Class)` when possible

### Issue 4: ClassCastException

**Error:**

```text
ClassCastException: com.cultodeportivo.Laptop cannot be cast to com.cultodeportivo.Alien
```

**Causes:**

- Wrong bean id retrieved
- Incorrect casting

**Solution:**

- Use type-safe `getBean("alien", Alien.class)` method
- Verify bean id before casting

## Key Takeaways

1. **ApplicationContext**: The core Spring IoC container that manages beans
2. **XML Configuration**: Defines beans using `<bean>` elements in XML files
3. **Early Instantiation**: Objects are created when ApplicationContext is initialized, not when `getBean()` is called
4. **spring-context Dependency**: Required for core Spring functionality
5. **Resource Location**: Configuration files must be in `src/main/resources/`
6. **Bean IDs**: Unique identifiers used to retrieve beans from the container
7. **Multiple Definitions**: Same class can have multiple bean definitions with different ids
8. **Type-Safe Retrieval**: Prefer `getBean(id, Class)` over casting
9. **Container-Managed**: Spring manages object lifecycle, not your application code
10. **Separation of Configuration**: XML separates configuration from code

## Summary

This foundational example demonstrates:

- How to set up a Spring project without Spring Boot
- The role of ApplicationContext in managing beans
- When and how Spring creates objects
- How to configure beans using XML
- How to retrieve and use beans from the container

Understanding these core concepts is essential before moving to more advanced Spring features or Spring Boot's auto-configuration mechanisms.
