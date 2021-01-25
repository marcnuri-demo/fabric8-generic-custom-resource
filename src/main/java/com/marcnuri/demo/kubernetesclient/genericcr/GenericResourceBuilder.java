package com.marcnuri.demo.kubernetesclient.genericcr;

import io.fabric8.kubernetes.api.builder.VisitableBuilder;
import io.fabric8.kubernetes.api.builder.Visitor;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;

import java.util.HashMap;

public class GenericResourceBuilder implements VisitableBuilder<GenericResource, GenericResourceBuilder> {

  private GenericResource genericResource;

  public GenericResourceBuilder(GenericResource item) {
    this.genericResource = new GenericResource();
    this.genericResource.setApiVersion(item.getApiVersion());
    this.genericResource.setKind(item.getKind());
    this.genericResource.setAdditionalProperties(new HashMap<>(item.getAdditionalProperties()));
    this.genericResource.setMetadata(item.getMetadata() == null ?
      new ObjectMeta() : new ObjectMetaBuilder(item.getMetadata()).build());
  }

  @Override
  public GenericResource build() {
    return genericResource;
  }

  @Override
  public GenericResourceBuilder accept(Visitor... visitors) {
    return null;
  }
}
