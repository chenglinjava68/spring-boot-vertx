package com.boot.spring.person;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by darlan on 25/01/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Person {

    private String id;
    private String name;
    private Integer age;
    private Integer weight;
    private String address;

    Person() {
    }

    private Person(String id, String name, Integer age,
                  Integer weight, String address) {
        this();
        this.id = id;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.address = address;
    }

    JsonObject asJson() {
        return Person.toJson(this);
    }

    static JsonObject toJson(Person data) {
        JsonObject json = new JsonObject();
        if(data.id != null)
            json.put("_id", data.id);
        if(data.name != null)
            json.put("name", data.name);
        if(data.age != null)
            json.put("age", data.age);
        if(data.weight != null)
            json.put("weight", data.weight);
        if(data.address != null)
            json.put("address", data.address);
        return json;
    }

    static Person fromJson(JsonObject data) {
        return new Person(data.getJsonObject("_id").getString("$oid"),
                data.getString("name"),
                data.getInteger("age"),
                data.getInteger("weight"),
                data.getString("address"));
    }

    @SuppressWarnings("unchecked")
    static List<Person> fromJson(JsonArray data) {
        List<Person> list = new ArrayList<>();
        if(data != null && !data.isEmpty()) {
            List<JsonObject> json = data.getList();
            list.addAll(json.stream().map(Person::fromJson)
                    .collect(Collectors.toList()));
        }
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", address='" + address + '\'' +
                '}';
    }

}
