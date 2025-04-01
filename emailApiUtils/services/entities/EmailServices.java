package utils.emailApiUtils.services.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailServices {
    GRAPH_API("graphapi");
    final String serviceType;

}
