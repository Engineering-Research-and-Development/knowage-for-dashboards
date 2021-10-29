package it.eng.demeter;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TEST_PlotCodesDS implements it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet{
	
	@Override
	public List getNamesOfProfileAttributeRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValues(Map profile, Map parameters) {
		String ds = "<ROWS><ROW Code=\"Plot1a\" Description=\"Field A\" /><ROW Code=\"Plot1b\" Description=\"Field B\" /></ROWS>";
		return ds;
	}

}
