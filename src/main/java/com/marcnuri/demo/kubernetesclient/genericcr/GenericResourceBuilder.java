package com.marcnuri.demo.kubernetesclient.genericcr;

import io.fabric8.kubernetes.api.builder.BaseFluent;
import io.fabric8.kubernetes.api.builder.VisitableBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;

import java.util.HashMap;

public class GenericResourceBuilder extends BaseFluent<GenericResourceBuilder>
  implements VisitableBuilder<GenericResource, GenericResourceBuilder> {

  private final GenericResource genericResource;
  private final ObjectMetaBuilder metadata;

  public GenericResourceBuilder(GenericResource item) {
    this.genericResource = new GenericResource();
    this.genericResource.setApiVersion(item.getApiVersion());
    this.genericResource.setKind(item.getKind());
    this.genericResource.setAdditionalProperties(new HashMap<>(item.getAdditionalProperties()));
    metadata = item.getMetadata() == null ? new ObjectMetaBuilder() : new ObjectMetaBuilder(item.getMetadata());
    _visitables.get("metadata").add(this.metadata);
  }

  @Override
  public GenericResource build() {
    genericResource.setMetadata(metadata.build());
    return genericResource;
  }

}
