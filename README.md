# Contentstack-Utils-Java

## Embedded Objects

This guide will help you get started with Contentstack Java Utils SDK to build apps powered by Contentstack.

### Prerequisites

- JDK 8 or later
- Contentstack account
- Latest version of IntelliJ IDEA / eclipse / vscode / spring tool suite

### SDK Installation and Setup

To setup [Utils SDK](https://mvnrepository.com/artifact/com.contentstack.sdk/utils) in your Java project, add the
following dependency in the pom.xml file

```java
<dependency>
<groupId>com.contentstack.sdk</groupId>
<artifactId>util</artifactId>
<version>{latest}</version>
</dependency>
```

If you are using Contentstack Java SDK, then the Utils SDK is already imported into your project, and the dependency
snippet will look as below

```java
<dependency>
<groupId>com.contentstack.sdk</groupId>
<artifactId>java</artifactId>
<version>{latest}</version>
</dependency>
```

Download [Latest](https://central.sonatype.dev/artifact/com.contentstack.sdk/utils/) dependency here

### Usage

Let’s learn how you can use Utils SDK to render embedded items.

### Create Render Option

To render embedded items on the front-end, use the renderContents function, and define the UI elements you want to show
in the front-end of your website, as shown in the example code below:

```java

package com.contentstack.utils;

import com.contentstack.utils.helper.Metadata;
import com.contentstack.utils.interfaces.NodeCallback;
import com.contentstack.utils.node.MarkType;
import com.contentstack.utils.render.DefaultOption;
import org.json.JSONObject;

public class DefaultOptionClass extends DefaultOption {

    @Override
    public String renderOptions(JSONObject embeddedObject, Metadata metadata) {
        switch (metadata.getStyleType()) {
            case BLOCK:
                return "<p>" + embeddedObject.getString("title") + "</p><span>" +
                        embeddedObject.getString("multi") + "</span>";
            case INLINE:
                return "<p>" + embeddedObject.getString("title") + "</p><span>" +
                        embeddedObject.getString("line") + "</span>";
            case LINK:
                return "<p>" + embeddedObject.getString("title") + "</p><span>" +
                        embeddedObject.getString("key") + "</span>";
            case DISPLAY:
                return "<p>" + embeddedObject.getString("someTitle") + "</p><span>" +
                        embeddedObject.getString("multi") + "</span>";
            default:
                return super.renderOptions(embeddedObject, metadata);
        }
    }

    @Override
    public String renderMark(MarkType markType, String renderText) {
        if (markType == MarkType.BOLD) {
            return "<b>" + renderText + "</b>";
        }
        return super.renderMark(markType, renderText);
    }

    @Override
    public String renderNode(String nodeType, JSONObject nodeObject, NodeCallback callback) {
        if (nodeType.equalsIgnoreCase("paragraph")) {
            String children = callback.renderChildren(nodeObject.optJSONArray("children"));
            return "<p class='class-id'>" + children + "</p>";
        }

        return super.renderNode(nodeType, nodeObject, callback);
    }
}


```

### Basic Queries

Contentstack Utils SDK lets you interact with the Content Delivery APIs and retrieve embedded items from the RTE field
of an entry.

#### Fetch Embedded Item(s) from a Single Entry

To get an embedded item of a single entry, you need to provide the stack API key, environment name, delivery token,
content type’s UID and entry’s UID. Then, use the includeEmbeddedItems function as shown below:

```java

import Contentstack
Stack stack=Contentstack.stack("apiKey","deliveryToken","environment_name");
        ContentType contentType=stack.contentType("content_type_uid");
        Entry entry=contentType.entry("entry_uid");
        entry.includeEmbeddedItems();
        entry.fetch(new EntryResultCallBack(){
@Override
public void onCompletion(ResponseType responseType,Error error){
        if(error==null){
        // [Success block]
        String[]keyPath={
        "rich_text_editor","global_rich_multiple.group.rich_text_editor"
        };
        JSONObject jsonObject=entry.toJSON();
        Utils.render(jsonObject,keyPath,new Option());
        }else{
        [Error block] // handle your error
        }}
        });
```

#### Fetch Embedded Item(s) from Multiple Entries

To get embedded items from multiple entries, you need to provide the stack API key, environment name, delivery token,
and content type’s UID.

```java

import Contentstack
Stack stack=Contentstack.stack("apiKey","deliveryToken","environment_name");
        Query query=stack.contentType("content_type_uid").query();
        query.includeEmbeddedItems();
        query.find(new QueryResultsCallBack(){
@Override
public void onCompletion(ResponseType responseType,QueryResult queryResult,Error error){
        if(error==null){
        List<Entry> entries=queryresult.getResultObjects();
        String[]keyPath={
        "rich_text_editor","global_rich_multiple.group.rich_text_editor"
        };
        for(Entry entry:entries){
        JSONObject jsonObject=entry.toJSON();
        Utils.render(jsonObject,keyPath,new Option());
        }
        }else{
        [Error block] // handle your error
        }}
        });
```

#### Render JSON RTE Contents

To get multiple entries, you need to provide the stack API key, environment name, delivery token, content type and entry
UID. Then, use the Utils.jsonToHTML function as shown below:

```java

import Contentstack
Stack stack=Contentstack.stack("apiKey","deliveryToken","environment_name");
        Query query=stack.contentType("content_type_uid").query();
        query.includeEmbeddedItems(); // include embedded items
        query.find(new QueryResultsCallBack(){
@Override
public void onCompletion(ResponseType responseType,QueryResult queryResult,Error error){
        if(error==null){
        List<Entry> entries=queryresult.getResultObjects();
        String[]keyPath={
        "rte_fieldUid","group.rteFieldUID"
        };
        for(Entry entry:entries){
        JSONObject jsonObject=entry.toJSON();
        Utils.jsonToHTML(jsonObject,keyPath,new Option());
        }
        }}
        });

```
