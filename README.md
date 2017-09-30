# proxy-switcher

[![Build Status](https://travis-ci.org/jntakpe/proxy-switcher.svg?branch=master)](https://travis-ci.org/jntakpe/devs-skills)
![license](https://img.shields.io/badge/license-MIT-blue.svg)


Enable or disable proxy settings for a set of tools

## Build

To build the application execute :  
```bash
./gradlew build 
```

## Run application

To run the application you need to provide the following arguments :

| Parameter                                | Values                | Format                      |
| ---------------------------------------- |:---------------------:| :--------------------------:|
| Enable or disable proxy                  | enable or disable     | -                           |
| List of application to enable or disable | bash,gradle           | Comma separated             |
| Proxy host value                         | some.proxy.host.value | Proxy host without protocol |
| Proxy port value                         | 8080                  | -                           |
| No proxy values                          | localhost,127.0.0.1   | Comma separated             |

For instance, to enable the proxies with the previous parameters : 
```bash
java -jar proxy-switcher-VERSION.jar enable bash,gradle,git,npm some.proxy.host.value 8080 localhost,127.0.0.1
```

## Supported proxies

* Bash
* Gradle
* Git
* Npm

## Supported platform

Right now only OSX and Windows platforms are supported
