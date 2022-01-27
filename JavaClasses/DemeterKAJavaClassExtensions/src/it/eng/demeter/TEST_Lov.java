package it.eng.demeter;

import java.util.List;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.behaviouralmodel.lov.bo.IJavaClassLov;

/**
 * @author albnale
 *
 */
public class TEST_Lov implements IJavaClassLov {

	@Override
	public String getValues(IEngUserProfile profile) {
		System.out.println("TEST LOV PROFILE ID:"+profile.getUserUniqueIdentifier());
		System.out.println("TEST LOV PROFILE ATTRIBUTES:"+profile.getUserAttributeNames());
		try {
			System.out.println("TEST LOV PROFILE ATTRIBUTES:"+profile.getUserAttribute("user_id"));
		} catch (EMFInternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "<ROWS> <ROW column1=\"1\" column2=\"a\" />\r\n<ROW column1=\"2\" column2=\"b\" />\r\n</ROWS>";
	}

	@Override
	public List getNamesOfProfileAttributeRequired() {
		return null;
	}

}
