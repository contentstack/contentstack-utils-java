# Contentstack-Utils-Java

### Embedded Objects

This guide will help you get started with Contentstack Java Utils SDK to build apps powered by Contentstack.

### Prerequisites
- JDK 8 or later
- Contentstack account
- Latest version of IntelliJ IDEA / Eclipse / VSCode

### SDK Installation and Setup
To setup Utils SDK in your Java project, add the following dependency in the pom.xml file

```java
<dependency>
    <groupId>com.contentstack.sdk</groupId>
    <artifactId>util</artifactId>
    <version>1.0.0</version>
</dependency>
```


If you are using Contentstack Java SDK, then the Utils SDK is already imported into your project, and the dependency snippet will look as below

```java
<dependency>
    <groupId>com.contentstack.sdk</groupId>
    <artifactId>java</artifactId>
    <version>1.6.0</version> // version 1.6.0 or above
</dependency>
```


### Usage
Let’s learn how you can use Utils SDK to render embedded items.

### Create Render Option
To render embedded items on the front-end, use the renderContents function, and define the UI elements you want to show in the front-end of your website, as shown in the example code below:

```java

Utils.renderContents(rteArray, localJsonObj, (embeddedObject, metadata) -> {
    
    switch (metadata.getStyleType()) {
        // in case you have embedded items using “block” option in the RTE
        case BLOCK:
            String title = embeddedObject.getString("title");
            String multi_line = embeddedObject.getString("multi_line");
            return "<p>" + title + "</p><span>" + multi_line + "</span>";
        
        // in case you have embedded items using “inline” option in the RTE
        case INLINE:
            String titleInline = embeddedObject.getString("title");
            String mlInline = embeddedObject.getString("multi_line");
            return "<p>" + titleInline + "</p><span>" + mlInline + "</span>";
        
        // in case you have embedded items using “link” option in the RTE
        case LINKED:
            String titleLinked = embeddedObject.getString("title");
            String mlLinked = embeddedObject.getString("multi_line");
            return "<p>" + titleLinked + "</p><span>" + mlLinked + "</span>";
            
        // in case you have embedded items using “display” option in the RTE
        case DISPLAY:
            String titleDiplayable = embeddedObject.getString("title");
            String mlDiplayable = embeddedObject.getString("multi_line");
            return "<p>" + titleDiplayable + "</p><span>" + mlDiplayable + "</span>";

        // in case you have embedded items using “display” option in the RTE
        case DOWNLOAD:
            String titleDownload = embeddedObject.getString("title");
            String mlDownload = embeddedObject.getString("multi_line");
            return "<p>" + titleDiplayable + "</p><span>" + mlDownload + "</span>";
            
        default:
           return null;
    }

});
```


### Basic Queries

Contentstack Utils SDK lets you interact with the Content Delivery APIs and retrieve embedded items from the RTE field of an entry.

#### Fetch Embedded Item(s) from a Single Entry
To get an embedded item of a single entry, you need to provide the stack API key, environment name, delivery token, content type’s UID and entry’s UID. Then, use the includeEmbeddedItems function as shown below:

```java

import Contentstack
Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment_name");
ContentType contentType = stack.contentType("content_type_uid");
Entry entry = contentType.entry("entry_uid");
entry.includeEmbeddedItems();
entry.fetch(new EntryResultCallBack() {
    @Override
    public void onCompletion(ResponseType responseType, Error error) {
        if (error == null) {
            // [Success block]
            String[] keyPath = {
            "rich_text_editor", "global_rich_multiple.group.rich_text_editor"
            };
            JSONObject jsonObject = entry.toJSON();
            Utils.render(jsonObject, keyPath, new Option());
        } else {
            [Error block]
        }}
});
```



#### Fetch Embedded Item(s) from Multiple Entries
To get embedded items from multiple entries, you need to provide the stack API key, environment name, delivery token, and content type’s UID.

```java

import Contentstack
Stack stack = Contentstack.stack("apiKey", "deliveryToken", "environment_name");
Query query = stack.contentType("content_type_uid").query();
query.includeEmbeddedItems();
query.find(new QueryResultsCallBack() {
    @Override
    public void onCompletion(ResponseType responseType, QueryResult queryResult, Error error) {
        if (error == null) {
            List<Entry> entries = queryresult.getResultObjects();
            String[] keyPath = {
            "rich_text_editor", "global_rich_multiple.group.rich_text_editor"
            };
            for (Entry entry : entries) {
                JSONObject jsonObject = entry.toJSON();
                Utils.render(jsonObject, keyPath, new Option());
            }
        }else{
        [Error block]
    }}
});
```


