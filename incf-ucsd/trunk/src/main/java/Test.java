import org.incf.ucsd.atlas.util.WHS2Paxinos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Test {

	private static final Logger LOG = LoggerFactory
	.getLogger(Test.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		WHS2Paxinos whs2paxinos = new WHS2Paxinos();
		String transformedCoordinateString = whs2paxinos.getTransformation( Long.parseLong("249.0".replace(".0", "")), 
				Long.parseLong("232"), Long.parseLong("222.0".replace(".0", "")) );

		LOG.debug("WHS to PAXINOS - TransformedCoordinateString - {}",transformedCoordinateString);

	}
	
}
