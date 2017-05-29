import ev3dev.robotics.tts.Espeak;

public class TTSDemo {

	public static void main(String[] args) {

		System.out.println("Testing Espeak on your EV3 Brick");

		Espeak TTS = new Espeak();

		//English example
		TTS.setVoice("en");
		TTS.setSpeedReading(105);
		TTS.setPitch(60);
		TTS.setMessage("I am a LEGO robot");
		TTS.say();
	}

}