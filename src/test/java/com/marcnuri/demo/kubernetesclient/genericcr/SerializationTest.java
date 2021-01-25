package com.marcnuri.demo.kubernetesclient.genericcr;

import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

class SerializationTest {

  @Test
  void deserializeCustomResource() throws IOException {
    // When
    final GenericResource result = Serialization.yamlMapper()
      .readValue(SerializationTest.class.getResourceAsStream("/strimzi-cr.yaml"), GenericResource.class);
    // Then
    assertThat(result)
      .hasFieldOrPropertyWithValue("metadata.name", "my-cluster")
      .extracting(GenericResource::getAdditionalProperties)
      .hasFieldOrPropertyWithValue("spec.kafka.version", "2.6.0")
      .hasFieldOrPropertyWithValue("spec.kafka.storage.type", "jbod");
  }

  @Test
  void serializeRandomResource() throws IOException {
    // Given
    final GenericResource input = new GenericResource();
    input.setMetadata(new ObjectMetaBuilder().withName("random-resource").build());
    input.setAdditionalProperties(new LinkedHashMap<>()); //Preserve insertion order for verification
    input.getAdditionalProperties().put("kind", "Kind");
    input.setAdditionalProperty("spec", Map.of(
      "field-1", "val-1",
      "field-2", "val-2"
    ));
    // When
    final String result = Serialization.yamlMapper().writeValueAsString(input);
    // Then
    assertThat(result).isEqualTo(
      "---\n" +
      "metadata:\n" +
      "  name: \"random-resource\"\n" +
      "kind: \"Kind\"\n" +
      "spec:\n" +
      "  field-1: \"val-1\"\n" +
      "  field-2: \"val-2\"\n"
    );
  }

  @Test
  void serializeRandomResourceInList() throws IOException {
    // Given
    final GenericResource gr = new GenericResource();
    gr.setKind("Kind");
    gr.setApiVersion("test.marcnuri.com/v1beta1");
    gr.setMetadata(new ObjectMetaBuilder().withName("random-resource").build());
    final KubernetesList kl = new KubernetesListBuilder()
      .addNewPodItem().withNewMetadata().withName("test-pod").endMetadata().endPodItem()
      .addToItems(gr)
      .build();
    // When
    final String result = Serialization.yamlMapper().writeValueAsString(kl);
    // Then
    assertThat(result).isEqualTo(
      "---\n" +
      "apiVersion: \"v1\"\n" +
      "kind: \"List\"\n" +
      "items:\n" +
      "- apiVersion: \"test.marcnuri.com/v1beta1\"\n" +
      "  kind: \"Kind\"\n" +
      "  metadata:\n" +
      "    name: \"random-resource\"\n" +
      "- apiVersion: \"v1\"\n" +
      "  kind: \"Pod\"\n" +
      "  metadata:\n" +
      "    name: \"test-pod\"\n"
    );
  }
}
