package io.github.tintinrevient.gis.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class GisMessage {

		  private Integer id;
		  private String message;
    private String timestamp;

				public GisMessage(Integer id, String message, String timestamp) {
					this.id = id;
					this.message = message;
					this.timestamp = timestamp;
				}
}
