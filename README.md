<a href="https://scan.coverity.com/projects/jla_01">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/22709/badge.svg"/>
</a>

# Relp Logging plugin for Java

See src/main/resources/logback.example.xml for exampe config

## Parameters
- relpHostAddress
Connection destination address
- relpPort
Connection destination port
- enableEventId48577
Enables structured data containing uuid and source information
- appName
Stream name
- hostname
Stream host
- connectionTimeout
Connection timeout
- reconnectInterval
Time to wait between re-connection attempts
- writeTimeout
Time to wait for destination to accept data
- readTimeout
Time to wait for destination to acknowledge sent data (low values cause duplicates)

## jboss-module

These instructions are untested but should work none the less.
jla_01-1.0.5-jboss-modules.jar contains logback appender for jboss as jboss-module. For generic information about jboss modules, see https://jboss-modules.github.io/jboss-modules/manual/

### Inclusion

Copy the jar into $EAP_HOME/modules.

Include module by using following convetion to target module.xml (which may be quite many).
```
<?xml version="1.0" encoding="UTF-8"?>
<module ...>
<.../>
<dependencies>
<module name="com.teragrep.jla_01"/>
</dependencies>
<.../>
</module>
```

One may wish to add this as a global module according to following link in order to avoid multiple inclusions:
https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html-single/configuration_guide/index#add_a_global_module

