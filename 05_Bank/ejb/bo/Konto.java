package bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Konto implements Serializable {
	private String nr;
	private String name;
	private Float saldo;
	private String currency;
}
