public class V {
	public static void main(String[] args) {
		Instrument[] stringQuartet = { new Violin(), new Violin(), new Viola(), new Cello() };
		// add here
        for (Instrument i : stringQuartet) {
            i.play(); 
        }
	}
}

abstract class Instrument {
	abstract public void play();
}

class Violin extends Instrument {
	@Override
	public void play() {
		System.out.println("旋律");
	}
}

class Viola extends Instrument {
	@Override
	public void play() {
		System.out.println("合旋");

	}
}

class Cello extends Instrument {
	@Override
	public void play() {
		System.out.println("低音");

	}
}
