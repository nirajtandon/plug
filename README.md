# Plug Framework

## What is Plug
Plug is a dependency injection framework that can be used to configure, and initialize an application.

## Why use Plug?
Plug is a small, initializes fast, and has no external dependencies. It works well if all you need is wiring the application components together, and initializing the application.

Plug also takes a different approach to configuration - Plug treats the components (or controls which are startable/stoppable components) as the key elements of the application - and the configuration. These are the only elements of the application configuration that is managed.

The idea is to treat the application configuration as not just a way to configure the application, but also a way to document the key application concepts and be able to identify the components (and controls) and their dependencies using the application configuration.    

## Configuration approach
Plug currently supports XML configuration only. This is planned to be extended to Java annotations, and a configuration DSL in subsequent releases.

## How Do I Integrate Plug Into My Application?
Plug can be integrated as a Maven dependency by including the following configuration.

### POM dependency

    <dependencies>
        <dependency>
            <groupId>com.fndef</groupId>
            <artifactId>plug</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>

### JFrog Repository in Maven Setting

https://fndef.jfrog.io/artifactory/default-maven-local

### Context Initialization

#### Initialize Context
        Context context = new ContextBuilder.WithOptions(ConfigFormat.XML)
                .configSource(getClass().getClassLoader().getResourceAsStream("simple-config.xml"))
                .getContext();

#### Refer Managed Components
        Test t = context.getById("c2", Test.class);
  
## Key Plug Concepts
Following are the key concepts of application configuration:
* Factories 
* Objects
* Components
* Controls

Each of these are covered in more detail in subsequent section, along with sample configuration snippets.

## Objects

Object is an object instance which is not managed but is used to resolve a managed component dependency. These are created and initialized by the container at the time of container startup.
Once created, references to these objects are injected into managed components during the application initialization.

The objects themselves are not managed by the container post the completion of the container initialization and any unreferenced objects are available for
garbage collection. Their only purpose is to satisfy managed component dependencies.

### Key object features:
* Each object has a unique Id
* Object is created by invoking it's default constructor or a custom constructor
* Container calls specific methods which can be setters or custom methods post object creation. These are used to initialize the object to a state where it is ready for use within the application
* Constructor and method invocations can reference other objects or pass values or refer to factories that return the required parameters
* Objects are not managed by the container and any unreferenced objects are available for garbage collection post container initialization
* An object referenced by an Id is created (or resolved) only once, and the same instance is returned every time that Id is referenced
	
### Example Configuration
#### Object from a factory or as a reference from another object
The attribute "ref" can refer to a factory that returns the object or it can be a reference to another object.

	<object id="id1" ref="f1" />

#### Create an object of a specific class 

Create an object of a class, and initialize it by invoking initializing methods before it is returned. The object is created only once and the same instance is returned every time it is referenced

	<object id="id2" type="class" >
		<constructor value="hello" />
		<constructor factory-ref="f1" />
		<constructor ref="id1" />
		<invoke method="someName1">
		    <param value="val1" />
		    <param value="5" converter="int" />
		    <param ref="id1" />
		</invoke>
		<invoke method="someNameLikeInit" />
	</object>
	
	
## Factories

Factory is used to create objects of a specific type. These can be used directly by the object definitions or by managed component definitions
to satisfy dependencies.

### Key factory features:
* Factory provides a completely initialized object of a specific type
* A factory delegates to a factory implementation for object creation
* A factory can either return a shared singleton or create a new object on each invocation. The behaviour depends on the factory implementation
* The objects returned by the factory are not managed by the container. These only serve to fulfil managed component dependencies
	
### Example Configuration
#### Call a static factory method
The factory might return the same instance or a new instance every time depending on the factory method implementation.

	<factory id="f1" type="class" method="m1" />
	
#### Call a factory method on an object instance
The "ref" attribute refers to any object or factory that can return an object instance.

The factory might return the same instance or a new instance every time depending on the factory method implementation.
	
	<factory id="f2" ref="id5" method="m2" />
	
	<object id="id5" type="aClass">
	....
	</object>
	 
	 -or-
	 
	 <factory id="id5" method="someMethod" />

#### Create Factory

Create factory instantiates a new instance every time the Id is referenced.

	<object id="id2" type="class" >
		<constructor value="hello" />
		<constructor factory-ref="f1" />
		<constructor ref="id1" />
		<invoke method="someName1">
		    <param value="val1" />
		    <param value="5" converter="int" />
		    <param ref="id1" />
		</invoke>
		<invoke method="someNameLikeInit" />
	</object>
	
## Managed Components

A managed component represents a specific application function or feature. Managed components are created, initialized and 
managed by the container. A managed component can reference other managed components. The dependencies between the components is resolved by 
the container during the container initialization. 

### Key managed component features:
* A managed component implements a specific functionality or feature
* A managed component can reference other objects, factories or other managed components to fulfil the functionality
* Plug container tracks managed components and these can be requested from the container using the component Id
* Managed components are eager initialized

### Example Configuration
	<component id="c1" type="class">
		<constructor value="hello" />
		<constructor ref="id1" />
		<constructor component-ref="id1" />
		<invoke method="someName1">
		    <param value="val1" />
		    <param ref="id1" />
		</invoke>
		<invoke method="someNameLikeInit" />
	</component>

## Managed Controls

A managed control is a type of managed component that is startable, and stoppable. A managed control often represents an feature or function or 
a shared resource that has a lifecycle associated with it.

### Key managed control features:
* A managed control implements a specific feature or function or represents a resource which has a lifecycle associated with it
* Is startable and stoppable
* Can reference other managed components, objects or factories to provide the function
* Plug container manages controls and a reference to the control can be request from the container 
* Manaed controls are eager initialized

### Example Configuration
	<control id="control1" type="class" >
		<constructor value="hello" />
		<constructor ref="id1" />
		<invoke method="someName1".
		    <param value="val1" />
		    <param ref="id1" />
		</invoke>
		<invoke method="someNameLikeInit" />
		<shutDown method="onContextStop" />
	</control>
	
## Future Road Map
Following are the features planned in future releases
* Support for annotation configuration
* Support for DSL configuration
* Container callbacks and lifecycle notifications for managed components 
* Auto parameter resolution and type conversions, support for placeholders
* Configuration visualization to identify application components and dependencies       
