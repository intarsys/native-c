# intarsys native-c

## Overview

This is "another" JNI wrapping library, but in some regards quite different.

The real memory / JNI access semantics is delegated to some concrete service implementation. 
Actually we don't provide one of our own, the default implementation we use is JNA.

The real strength of this library is its high level model of JNI access, allowing more easy and 
understandable API's. We feel that at this point most of the candidates we reviewed
are quite poor or hard to understand.

The design goals were as follows:

1. Provide access to all levels of a native interface. As we can not forsee what different libaries and systems will work together, access is provided from bottom up using the plain address as the least common denominator.
2. Provide clean abstractions
3. Differentiate between a "pointer" or "memory handle" and the data semantics. This allows to work as well using plain pointer arithmetics as object state access.

The high level model provides the following abstractions:

1. Interface The concrete implementation for accessing JNI. This interface is quite small and allows for the adaption of many JNI libraries out there. It should not be to hard to create one of your own.
2. Library The loadable library providing state and behavior.
3. Function The behavior part of the library, accepting parameters and return results. 
4. Handle An abstraction of a pointer, without any data specific features. This could be viewed as a plain wrapper around an address, another representation for "void *".
5. Object The data referenced by a handle (or pointer). The object deals with the bytes referenced by the handle.

The object defines the semantics of how to interpret the memory referenced by the handle. 
The handle defines "where" to find data, the object defines "what" data you will find there - 
it deals only with the "dereferenced" part of the handle.

On the C side, the object is always represented by the handle / pointer. This means that an object
is always used and forwarded by reference. Do not confuse this with the handling on primitive
types. In this code snippet

  
	int a;
	NativeInt b = new NativeInt();
	...
	int result = function.invoke(Integer.class, a, b);
	...

the function is called using the value of "a" and a pointer to "b".

This library currently does not provide any convenience for some aspects of 
native calls, for example

- primitive arrays
- float / double support
- String encodings
- callbacks
- maybe other features we didn't need until now and so are not aware of..

The underlying infrastructure (JNA) may handle such constructs, but as long as the 
semantics is not defined on this library level, you should expect unportable 
behavior. 

We propose, even if not convenient, to express all call semantics using documented 
features only. Feel free to ask for the inclusion of more library features.

## Build

The project is divided into an API project and a JNA implementation project. 
Both are built using Maven using the ```clean install``` standard pattern:

	mvn clean install


## Dependencies

This library consists of a platform independent part and a provider implementation. A provider based on JNA is included and as such you need the jna.jar to compile and run.

## Usage

### Common scenarios

#### Call functions with primitive types

The most simple calls use only primitive types. These are mapped by the "invoke" itself
using builtin rules to target the C types.

The C part may look like this:

	int intValue;
	float floatValue;
	...
	int result = function(intValue, floatValue);
	...

An equivalent call from Java would look like:

	int intValue;
	float floatValue;
	...
	int result = function.invoke(Integer.class, intValue, floatValue);
	...
	...

This seems straightforward....

#### Call functions with String parameters

The first problems are expected when dealing with strings or char* on the C side. But
no problem here:

The C part may look like this:

	char* string = "test";
	...
	int result = function(string);
	...


An equivalent call from Java would look like:

	String string = "test";
	...
	int result = function.invoke(Integer.class, string);
	...

String semantics are handled internally. Even the platform wide character features 
can be accessed quite easy - simply wrap the String in a "WideString" to indicate the
special treatment on invocation marshalling.


	String string = "test";
	WideString wideString = new WideString(string);
	...
	int result = function.invoke(Integer.class, wideString);
	...

In much the same way you can request a wide string conversion on the result.

	...
	WideString result = function.invoke(WideString.class);
	String string = result.toString();
	...

#### Call with parameters "by reference" (out parameters)

Other functions return values via "out" parameters, via references to C memory.

The C part may look like this:

	int value;
	
	...
	int result = function(&value);
	if (value == 42) {
		...
	}
	...

An equivalent call from Java would look like:

	NativeInt value = new NativeInt();

	int result = function.invoke(Integer.class, value);
	if (value.intValue() == 42) {
		...
	}
	...

The NativeObject is allocated in C memory and forwarded "by reference".

This call pattern stays the same regardless of the complexity of the NativeObject - so this is 
the call for your Java side definition of the C struct:

	// allocates memory for the struct in C  
	MyStruct value = new MyStruct();

	// call function with a pointer to the new struct
	int result = function.invoke(Integer.class, value);
	...

#### Wrapping pointers returned from out parameters

With C you find quite often a pointer to a newly create memory chunk returned via 
an out parameter.

	my_struct *value;
	...
	function(&value);
	int a = value->a;
	...

This is one of the rare occasions you will deal with the "NativeReference" directly. 
If we follow exactly the pattern we have used so far we get:

	// allocate memory for holding a pointer to a struct
	NativeReference ptrValue = new NativeReference(MyStruct.META);
	// call the function with the pointer to the pointer 
	int result = function.invoke(Integer.class, ptrValue);
	// dereference the result...
	MyStruct value = (MyStruct)ptrValue.getValue();

Here's a common pattern to manage "transparent" handles that do not designate data
structures you should deal with - for sure you can declare a NativeVoid subclass 
of you own for better readability or to add methods.

	// allocate memory for holding a void pointer
	NativeReference ptrValue = new NativeReference(NativeVoid.META);
	// call the function with the pointer to the pointer 
	int result = function.invoke(Integer.class, value);
	// dereference the result...
	NativeVoid value = (NativeVoid)ptrValue.getValue();

#### Wrapping a pointer returned as function result

Many functions return handles to newly created objects. Wrapping this result to the 
INativeObject framework is simply a matter of declaration:

	NativeVoid result = function.invoke(NativeVoid.class, ...);

You have just wrapped a "void *" (void pointer)! Notice how you "hide" the C reference or pointer 
semantics. The less you think about it, the more it is intuitive :-). Using this code
will return "null" in case of a "0" address.

NativeVoid is a "stateless" object, declaring to be of size "0". This is often the case with transparent handles 
to some proprietary / private information. Only the handle itself is used to manipulate state via the associated 
library functions.

In much the same way you can create a NativeStruct instances, holding public state information
and allowing easy access to it.

Assume you have defined a NativeStruct subclass "MyStruct" of any memory layout. The above 
code sequence changes to

	MyStruct structObj = function.invoke(MyStruct.class, ...);

Behind this code is a simple two step process. You can do it manually if the default is
not exactly what you want, for example in some cases where an address of "-1" means failure.

- Create the INativeHandle wrapper. This object represents a memory address and implements some access primitives
- Use the INativeHandle to build the INativeObject wrapper that is situated at this memory location. This can be any of the INativeObject subclasses, such as a primitive, composite or even void representation. 


	int address = function.invoke(Integer.class, ...);
	if (address == 0) {
		return null;
	}
	INativeHandle handle = NativeInterface.get().createHandle(address);
	return NativeVoid.META.createNative(handle);

The pointer, represented by the INativeHandle is wrapped by a NativeObject which is
what is referenced by the pointer. 

#### Primitive arrays and buffers

You can call a native function using primitive arrays the same way as using plain primitives.
The basic marshalling will map the values in both direction, so you see C side changes in the 
Java array directly. 

Conceptually you should treat such a call as follows:
The memory is managed by the library. Upon entry, memory is allocated in C address space, the data is copied. 
The C function is called with the pointer to the newly allocated memory. After 
termination of the function, memory is copied back  to Java address space, and the 
newly allocated memory is discarded. Access to the memory in C address space is no longer valid!

So, if you want to provide a buffer to C for use even outside the function call this
is not the correct way! You must use NativeObject instances, for example NativeBuffer,
to force C memory allocation resident in memory (controlled by your application). 

To optimize the marshalling between Java and C you may use direct "ByteBuffer" objects. Using
this objects marshalling MAY be optimized by the underlying implementation to share memory
between Java and C.

In any case you may use a NativeBuffer and access the memory allocated in heap space from both 
sides. NativeBuffer can be seen as an explicit equivalent to a DirectBuffer.

## License

<pre>
/*
 * Copyright (c) 2013, intarsys consulting GmbH
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of intarsys nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
</pre>

## Service & Support

Service & support should be funneled through the tools available with Github.

If you need further assistance, contact us.

<pre> 
intarsys consulting GmbH
Kriegstrasse 100
76135 Karlsruhe
Fon +49 721 38479-0
Fax +49 721 38479-60
info@intarsys.de
www.intarsys.de
</pre>
