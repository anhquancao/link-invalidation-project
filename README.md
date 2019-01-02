# Information Integration

## File Description
- Property.java

Property which is used in PropertyMapping.java

- PropertyMapping.java

Create a property map, with a structure like this:
```{
    http://oaei.ontologymatching.org/2010/IIMBDATA/en/vince_vaughn: {
        rdf:resource: {
             weight: 1,
             isFunctional: true, //is a funcational property
         },
        rdf:datatype:  {
             weight: 1,
             isFunctional: false, //is not a funcational property
         },
     }
 }```
 
- Detection.java
TODO: Compare two maps of onto files with refalign file

## External Requirement
1. Dataset Folder: ../iimb_large_30082010/IIMB_LARGE/

## How to Get Started
1. Import maven project 

2. Run Detection.java
