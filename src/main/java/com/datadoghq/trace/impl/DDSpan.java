package com.datadoghq.trace.impl;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonGetter;

import io.opentracing.Span;
import io.opentracing.SpanContext;


public class DDSpan implements io.opentracing.Span {

    protected final Tracer tracer;
    protected final String operationName;
    protected Map<String, Object> tags;
    protected long startTime;
    protected long durationNano;
    protected final DDSpanContext context;

    DDSpan(
            Tracer tracer,
            String operationName,
            Map<String, Object> tags,
            Optional<Long> timestamp,
            DDSpanContext context) {
        this.tracer = tracer;
        this.operationName = operationName;
        this.tags = tags;
        this.startTime = timestamp.orElse(System.nanoTime());
        this.context = context;
    }

    public SpanContext context() {
        return this.context;
    }

    public void finish() {
        this.durationNano = System.nanoTime() - startTime;
    }

    public void finish(long nano) {
        this.durationNano = nano;
    }

    public void close() {
        this.finish();
    }

    public io.opentracing.Span setTag(String tag, String value) {
        return this.setTag(tag, value);
    }

    public Span setTag(String tag, boolean value) {
        return this.setTag(tag, value);
    }

    public Span setTag(String tag, Number value) {
        return this.setTag(tag, (Object) value);
    }

    private Span setTag(String tag, Object value) {
        this.tags.put(tag, value);
        return this;
    }

    public Span log(Map<String, ?> map) {
        return null;
    }

    public Span log(long l, Map<String, ?> map) {
        return null;
    }

    public Span log(String s) {
        return null;
    }

    public Span log(long l, String s) {
        return null;
    }

    public Span setBaggageItem(String key, String value) {
        this.context.setBaggageItem(key, value);
        return this;
    }

    public String getBaggageItem(String key) {
        return this.context.getBaggageItem(key);
    }

    public Span setOperationName(String s) {
        return null;
    }

    public Span log(String s, Object o) {
        return null;
    }

    public Span log(long l, String s, Object o) {
        return null;
    }

    //Getters and JSON serialisation instructions
    
    @JsonGetter(value="name")
    public String getOperationName() {
        return operationName;
    }
    
    @JsonGetter(value="meta")
    public Map<String, Object> getTags() {
        return this.tags;
    }

    @JsonGetter(value="start")
    public long getStartTime() {
        return startTime * 1000000;
    }
    
    @JsonGetter(value="duration")
    public long getDurationNano(){
    	return durationNano;
    }
    
    public String getService(){
    	return context.getServiceName();
    }
    
    @JsonGetter(value="trace_id")
    public long getTraceId(){
    	return context.getTraceId();
    }
    
    @JsonGetter(value="span_id")
    public long getSpanId(){
    	return context.getSpanId();
    }
    
    @JsonGetter(value="parent_id")
    public long getParentId(){
    	return context.getParentId();
    }
    
    @JsonGetter(value="resource")
    public String getResourceName(){
    	return context.getResourceName()==null?getOperationName():context.getResourceName();
    }
    
    public String getType(){
    	return context.getSpanType();
    }
    
    public int getError(){
    	return context.getErrorFlag()?1:0;
    }
}