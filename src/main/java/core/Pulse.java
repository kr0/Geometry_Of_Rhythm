package core;

/**
 * A pulse is either an Attack, a rest, or an Accented attack.
 * These are represented in box notation as "x", ".", and "X" respectively.
 * @author kr0
 *
 */
enum Pulse {
	ATTACK("x"), REST("."), ACCENT("X");
	
	private String repr;
	Pulse(String _repr){
		repr = _repr;
	}
	
	@Override
	public String toString() {
		return repr;
	}
	
}
