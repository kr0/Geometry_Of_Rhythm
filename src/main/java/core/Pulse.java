package core;

public enum Pulse {

	REST("."), ACCENT("X"), ATTACK("x");
	
	private String repr;
	private Pulse(String _repr){
		repr = _repr;
	}
	
	
	@Override
	public String toString() {
		return repr;
	}
}