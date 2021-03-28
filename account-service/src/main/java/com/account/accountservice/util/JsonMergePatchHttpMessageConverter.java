package com.account.accountservice.util;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import java.io.IOException;

@Component
public class JsonMergePatchHttpMessageConverter extends AbstractHttpMessageConverter<JsonMergePatch> {

  public JsonMergePatchHttpMessageConverter() {
    super(MediaType.valueOf("application/merge-patch+json"));
  }

  @Override
  protected boolean supports(Class<?> aClass) {
    return JsonMergePatch.class.isAssignableFrom(aClass);
  }

  @Override
  protected JsonMergePatch readInternal(Class<? extends JsonMergePatch> aClass, HttpInputMessage httpInputMessage)
      throws IOException, HttpMessageNotReadableException {

    try(JsonReader reader = Json.createReader(httpInputMessage.getBody())) {
      return Json.createMergePatch(reader.readValue());
    } catch (Exception e) {
      throw  new HttpMessageNotReadableException(e.getMessage(), httpInputMessage);
    }
  }

  @Override
  protected void writeInternal(JsonMergePatch jsonMergePatch, HttpOutputMessage httpOutputMessage)
      throws IOException, HttpMessageNotWritableException {

    try(JsonWriter writer = Json.createWriter(httpOutputMessage.getBody())) {
      writer.write(jsonMergePatch.toJsonValue());
    } catch (Exception e) {
      throw new HttpMessageNotWritableException(e.getMessage(), e);
    }
  }
}
