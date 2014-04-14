package ie.gmit.Fuzzy;

public class MarkSchema {
	private double distance; // 0-20;
	private double frequency; // 0-10;
	private double position; // 0-6; 2-4||4-6

	public MarkSchema( double position, double frequency, double distance) {
		this.distance = distance > 20 ? 20 : distance;
		this.frequency = frequency > 10 ? 10 : frequency;
		this.position = position > 6 ? 6 : position;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * @return the frequency
	 */
	public double getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the position
	 */
	public double getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(double position) {
		this.position = position;
	}

}
