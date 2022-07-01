# AlphaId

Random pseudo-unique string id generator with a default web friendly alphabet.

Based on https://github.com/aventrix/jnanoid
- Written in kotlin
- Performance improvements
- Allow creating a reusable instance with custom alphabet and/or random generator


## Secure

Uses [SecureRandom](https://docs.oracle.com/javase/7/docs/api/java/security/SecureRandom.html) by 
default to generate cryptographically strong random IDs with a good distribution of characters.


## Compact

Generates compact IDs. By using a larger alphabet than UUID, AlphaId can generate a greater number of unique 
IDs in the same space, when compared to UUID.


## URL-Friendly

Uses URL-friendly characters (`A-Za-z0-9_-`). Perfect for unique identifiers in web applications.


## Customizable

All default options may be overridden. Supply your own Random Number Generator, alphabet, or size.


## Tested

Tested thoroughly. Look in the test to see sample usages.


## Dependency


### Maven

```xml
<dependency>
  <groupId>com.semanticmart.alphaid</groupId>
  <artifactId>alphaid</artifactId>
  <version>0.0.0</version>
</dependency>
```


### Gradle

**Groovy DSL**

```groovy
compile 'com.semanticmart.alphaid:alphaid:0.0.0'
```

**Kotlin DSL**
```kotlin
implementation("com.semanticmart.alphaid:alphaid:0.0.0")
```


## Usage

Provides one easy-to-use utility class (`AlphaId`) with methods to generate IDs.


### Default IDs - `randomId()`

The default method creates secure, url-friendly, unique ids. It uses an url-friendly alphabet (`A-Za-z0-9_-`), 
a secure random number generator, and generates a unique ID with 32 characters.

```kotlin
val id = AlphaId.random()
// id is something like `DVUQIrcvdhuAZhEWYapF58j4_m-5pT6j`
```


### Specify length - `randomNanoId(length)`

An additional method allows to generate custom IDs by specifying a custom length.

```kotlin
val id = AlphaId.random(4)
// id is something like `5Osn`
```

`AlphaId.random(21)` generates an id with the smaller length than a UUID (32 characters) with a greater number of 
unique IDs.


### Customize alphabet and/or random generator

```kotlin
val alphaId = AlphaId(alphabet = "xyz".toCharArray())
val id1 = alphaId.next(10)
// id1 is something like `xxzxzyyyzy`
val id2 = alphaId.next(10)
// id2 is something like `yyyxyzzzxz`

val alphaId = AlphaId(randomGenerator = Random(123))
val id1 = alphaId.next(10)
// id1 is `rLvTj2WWfa`
val id2 = alphaId.next(10)
// id2 is `kfl_MAUg3l`
```

## License

This project is licensed under the terms of the [MIT license](./LICENSE).

Based on the [NanoId](https://github.com/ai/nanoid) by [Andrey Sitnik](https://github.com/ai/) and 
[JNanoId](https://github.com/aventrix/jnanoid) by [Aventrix](https://github.com/aventrix).
