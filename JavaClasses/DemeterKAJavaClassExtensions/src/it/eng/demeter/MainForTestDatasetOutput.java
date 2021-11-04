package it.eng.demeter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import it.eng.demeter.a3.A3Dataset;
import it.eng.demeter.a5.A5Dataset;
import it.eng.demeter.d3.D3Dataset;
import it.eng.demeter.b1.B1Dataset;
import it.eng.demeter.d2.D2Dataset;
import it.eng.demeter.c1.C1Dataset;
import it.eng.demeter.e2.E2Dataset;
import it.eng.demeter.i2.I2Dataset;
import it.eng.demeter.i3.I3Dataset;
import it.eng.demeter.f1.F1Dataset;
import it.eng.demeter.g1.G1Dataset;
import it.eng.demeter.a2.A2Dataset;
import it.eng.demeter.e1.E1Dataset;
import it.eng.demeter.g2.G2Dataset;
import it.eng.demeter.h2.H2Dataset;
import it.eng.demeter.f2.F2Dataset;
import it.eng.demeter.i1.I1Dataset;
import it.eng.demeter.c2.C2Dataset;

public class MainForTestDatasetOutput {

	public static void main(String[] args) {
		Map<String, String> profile = new HashMap<String, String>();
		Map<String, String> parameters = new HashMap<String, String>();
		
		D3Dataset d3 = new D3Dataset();
		G1Dataset g1 = new G1Dataset();
		B1Dataset b1 = new B1Dataset();
		D2Dataset d2 = new D2Dataset();
		C1Dataset c1 = new C1Dataset();
		E2Dataset e2 = new E2Dataset();
		I2Dataset i2 = new I2Dataset();
		F1Dataset f1 = new F1Dataset();
		A2Dataset a2 = new A2Dataset();
		E1Dataset e1 = new E1Dataset();
		G2Dataset g2 = new G2Dataset();
		H2Dataset h2 = new H2Dataset();
		F2Dataset f2 = new F2Dataset();
		I1Dataset i1 = new I1Dataset();
		C2Dataset c2 = new C2Dataset();
		I3Dataset i3 = new I3Dataset();
		A5Dataset a5 = new A5Dataset();
		try {
		      FileWriter myWriter = new FileWriter("C:\\Users\\luidicorra\\Desktop\\Test\\filename.xml");
		      myWriter.write(f1.getValues(profile, parameters));
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }

	}

}
