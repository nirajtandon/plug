# Plug Dependency Injection Framework

Plug is a dependency injection framework that builds, configures and initialises application components.

## Objects

Object is a POJO instance which is used by managed components. These are created and initialized by the container at the time of container startup.
Once created, these can be injected into managed components during the container startup, and as part of the container initialization.

The objects themselves are not managed by the container post the completion of the container initialization and any unreferenced objects are available for
garbage collection. Their only purpose is to satisfy managed component dependencies.

### Key object features:
* Each object has a unique Id
* Object is created by invoking it's default constructor or a custom constructor
* Container calls specific methods which can be setters or custom methods post object creation
* Constructor invocation and method calls can reference other objects or pass values
* Once the object is created and methods called, custom object initialization can be performed
* A factory can be used to create an object or passed to satisfy object dependencies
* Objects are not managed by the container and any unreferenced objects are available for garbage collection post container initialization
	
### Example Configuration
	<object id="id1" factory-ref="f1" />
	
	<object id="id2" type="class" >
		<constructor value="hello" />
		<constructor factory-ref="f1" />
		<constructor ref="id1" />
		<invoke name="someName1" value="val1" />
		<invoke name="someName2" ref="id1" />
		<invoke name="someName3" factory-ref="f1" />
		<invoke name="someNameLikeInit" />
	</object>
	
	
## Factories

Factory is used to create objects of a specofic type. These can be used directly by the object definitions or by managed component definitions
to satisfy dependencies.

### Key factory features:
* Factory provides a completely initialized object of a specific type
* A factory delegates to a factory implmentation for object creation
* A factory can either return a shared singleton or create a new object on each invocation, depeding on the factory implmentation
* The objects returned by the factory are not managed by the container
	
### Example Configuration
	<factory id="f1" type="class" method="m1" />
	
	<factory id="f2" ref="id5" method="m2" />
	
	<factory id="f3" object-type="class" >
		<constructor value="hello" />
		<constructor factory-ref="f1" />
		<constructor ref="id1" />
		<invoke name="someName1" value="val1" />
		<invoke name="someName2" ref="id1" />
		<invoke name="someName3" factory-ref="f1" />
		<invoke name="someNameLikeInit" />
	</factory>
	
## Managed Components

A managed component represents a specific function or a feature provided by the application. Managed components are created, initialized and 
managed by the container. A managed component can reference other managed components. The dependencies between the components is resolved by 
the container during the container initialization. 

### Key managed component features:
* A managed component implements a specific functionality or feature
* A managed component can reference objects, factories or other managed components to fulfil the functionality

### Example Configuration
	<component id="c1" type="class">
		<constructor value="hello" />
		<constructor factory-ref="f1" />
		<constructor ref="id1" />
		<constructor component-ref="id1" />
		<invoke name="someName1" value="val1" />
		<invoke name="someName2" ref="id1" />
		<invoke name="someName3" factory-ref="f1" />
		<invoke name="someName2" component-ref="id1" />
		<invoke name="someNameLikeInit" />
		<shutDown method="cleanup" />
	</component>

## Managed Controls

A managed control is a type of managed component that is startable, and stoppable. A managed control often represents an feature or function or 
a shared resource that has a lifecycle associated with it.

### Key managed control features:
* A managed control implements a specific feature or function or represents a resource which has a lifecycle associated with it
* Is startable and stoppable
* Can reference other managed components, objects or factories to provide the function

### Example Configuration
	<control id="control1" type="class" >
		<constructor value="hello" />
		<constructor factory-ref="f1" />
		<constructor ref="id1" />
		<constructor component-ref="id1" />
		<invoke name="someName1" value="val1" />
		<invoke name="someName2" ref="id1" />
		<invoke name="someName3" factory-ref="f1" />
		<invoke name="someName2" component-ref="id1" />
		<invoke name="someNameLikeInit" />
		<startUp method="start" />
		<shutDown method="stop" />
	</control>
	

