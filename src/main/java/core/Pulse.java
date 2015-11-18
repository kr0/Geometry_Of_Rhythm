package core;

public final class Pulse {

	private String repr;
	public static String REST = ".";
	public static String ACCENT = "X";
	public static String ATTACK = "x";
	
	private Pulse(String _repr){
		repr = _repr;
	}
	
	public static Pulse Rest(){
		return new Pulse(REST);
	}
	
	public static Pulse Accent(){
		return new Pulse(ACCENT);
	}
	
	public static Pulse Attack(){
		return new Pulse(ATTACK);
	}
	
	@Override
	public String toString() {
		return repr;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Pulse)){
			return false;
		}
		Pulse o = (Pulse)obj;
		return this.repr.equals(o.repr);
	}
}
