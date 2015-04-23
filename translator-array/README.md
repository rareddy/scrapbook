# arrayiterate

Build the project using
```
git clone https://github.com/rareddy/scrapbook.git
cd scrapbook/translator-array
mvn clean install
```

Now in the target directory, unzip the 
```
translator-array-0.1-SNAPSHOT-jboss-as7-dist.zip
```

into "modules" directory of the DV 6.1.0

Add following to the standalone.xml in the "teiid" subsystem
```
       <translator name="array" module="org.jboss.teiid.translator.array"/>
```

Now Given a Dynamic VDB like

```
<vdb name="arraytest" version="1">
    <model visible="true" name="arrayiterate">
        <source name="array" translator-name="array"/> 
    </model>
</vdb>
```

You can execute queries like

```
select * from (exec "arrayiterate"."ARRAYITERATE"((1,2,3))) x
```

which will return resutls like

```
result
1
2
3
```
