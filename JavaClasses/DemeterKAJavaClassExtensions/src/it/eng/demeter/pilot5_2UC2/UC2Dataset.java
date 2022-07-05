package it.eng.demeter.pilot5_2UC2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.util.HtmlUtils;

import it.eng.demeter.DemeterAbstractJavaClassDataSet;

public class UC2Dataset extends DemeterAbstractJavaClassDataSet {
	private String url = null;
	
	@Override
	public String getValues(Map profile, Map parameters) {
		String userId = (String) profile.get("user_id");
		logger.info("user id ->" + userId + "<-");
		System.out.println("USER ID: "+userId);
		
		try {
			url = parameters.get(URL).toString();
			logger.info("base url ->" + url + "<-");
			System.out.println("base url ->" + url + "<-");
			
			url = url + "/" + userId;
			url = url.replaceAll("\'", "");
			
			logger.info("final url ->" + url + "<-");
			System.out.println("final url ->" + url + "<-");
			
			Object removedUrl = parameters.remove(URL);
			
			logger.info("removed url ->" + removedUrl + "<-");
			System.out.println("removed url ->" + removedUrl + "<-");
			
			parameters.put(URL, url);
			
			logger.info("putted url ->" + url + "<-");
			System.out.println("putted url ->" + url + "<-");
		} catch (Exception e) {
			e.printStackTrace();
	    	logger.error(e.getMessage(), e.getCause());
		}
		
		return super.getValues(profile, parameters);
	}
	
	@Override
	protected String aimTranslator(StringBuilder aim) throws Exception, JSONException {
		String rows = "";
		rows = "<ROWS>";
		
		Map<String, UC2Animal> animals = new HashMap<String, UC2Animal>();
		Map<String, UC2AnimalGroup> animalGroups = new HashMap<String, UC2AnimalGroup>();
		Map<String, UC2Farm> farms = new HashMap<String, UC2Farm>();
		Map<String, UC2Observation> observations = new HashMap<String, UC2Observation>();
		
		try {
			/*Convert AIM to JSON*/
			JSONObject jsonObject = new JSONObject(aim.toString());  
			JSONArray jsonArray = new JSONArray(jsonObject.get("@graph").toString());

			/*Reading elements*/
			for (int l=0; l<jsonArray.length(); l++){
				String elemento = jsonArray.getJSONObject(l).get("@type").toString();
				switch (elemento) {
				case "AnimalGroup":
					UC2AnimalGroup ag = new UC2AnimalGroup();
					ag.setId(jsonArray.getJSONObject(l).get("@id").toString());
					ag.setName(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("name").toString()));
					ag.setDescription(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("description").toString()));
					ag.setAnalysisDate(jsonArray.getJSONObject(l).get("analysisDate").toString().split("T")[0]);
					animalGroups.put(ag.getId(), ag);
					break;
				case "Farm":
					UC2Farm f = new UC2Farm();
					f.setId(jsonArray.getJSONObject(l).get("@id").toString());
					f.setFarmID(jsonArray.getJSONObject(l).get("identifier").toString());
					f.setName(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("name").toString()));
					f.setDescription(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("description").toString()));
					f.setAddress(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("address").toString()));
					f.setGeoLocation(jsonArray.getJSONObject(l).getJSONObject("hasGeometry").get("asWKT").toString());
					farms.put(f.getId(), f);
					break;
				case "Animal":
					UC2Animal a = new UC2Animal();
					a.setId(jsonArray.getJSONObject(l).get("@id").toString());
					a.setAnimalID(jsonArray.getJSONObject(l).get("identifier").toString());
					a.setAnimalSpecies(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("animalSpecies").toString()));
					a.setEarTag(jsonArray.getJSONObject(l).get("legalID").toString());
					a.setBirthdate(jsonArray.getJSONObject(l).get("birthdate").toString().split("-")[0]);
					a.setSex(jsonArray.getJSONObject(l).get("sex").toString());
					a.setBreed(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("breed").toString()));
					a.setLocatedAtFarm(jsonArray.getJSONObject(l).get("locatedAt").toString());
					if (jsonArray.getJSONObject(l).get("healthCondition").toString().equals("healthy")) {
						a.setHealthCondition("1");
					} else {
						a.setHealthCondition("0");
					}
					a.setIsMemberOfAnimalGroup(jsonArray.getJSONObject(l).get("isMemberOfAnimalGroup").toString());
					a.setNotes(HtmlUtils.htmlEscapeDecimal(jsonArray.getJSONObject(l).get("notes").toString()));
					a.setVisibility(jsonArray.getJSONObject(l).get("visibility").toString());
					a.setDepiction(jsonArray.getJSONObject(l).get("depiction").toString());
					animals.put(a.getId(), a);
					break;
				case "Observation":
					UC2Observation o = new UC2Observation();
					o.setId(jsonArray.getJSONObject(l).get("@id").toString());
					o.setOrder(jsonArray.getJSONObject(l).get("order").toString());
					o.setHasFeatureOfInterest(jsonArray.getJSONObject(l).get("hasFeatureOfInterest").toString());
					o.setHasSimpleResult(jsonArray.getJSONObject(l).get("hasSimpleResult").toString());
					o.setResultTime(jsonArray.getJSONObject(l).get("resultTime").toString().split("T")[0]);
					observations.put(o.getId(), o);
					break;
				}
			}
			
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
			e.printStackTrace();
		}

		/*Creating records for dataset*/
		List<UC2DatasetRecord> dsRList = new ArrayList<UC2DatasetRecord>();
		animals.forEach((id, animal) -> {
			UC2DatasetRecord dsR = new UC2DatasetRecord();
			String uID = "";
			dsR.setEarTag(animal.getEarTag());
			dsR.setBreed(animal.getBreed());
			dsR.setSex(animal.getSex());
			dsR.setBorn(animal.getBirthdate());
			dsR.setPos(animal.getHealthCondition());
			dsR.setExtra(animal.getVisibility());
			dsR.setComments(animal.getNotes());
			dsR.setPicture(animal.getDepiction());
			if (animalGroups.containsKey(animal.getIsMemberOfAnimalGroup())) {
				dsR.setAnimalGroupName(animalGroups.get(animal.getIsMemberOfAnimalGroup()).getName());
				dsR.setAnimalGroupType(animalGroups.get(animal.getIsMemberOfAnimalGroup()).getDescription());
				dsR.setAnimalGroupDate(animalGroups.get(animal.getIsMemberOfAnimalGroup()).getAnalysisDate());
				uID = animalGroups.get(animal.getIsMemberOfAnimalGroup()).getId();
			}
			if (farms.containsKey(animal.getLocatedAtFarm())) {
				dsR.setFarmName(farms.get(animal.getLocatedAtFarm()).getName());
				dsR.setFarmDescription(farms.get(animal.getLocatedAtFarm()).getDescription());
				dsR.setFarmAddress(farms.get(animal.getLocatedAtFarm()).getAddress());
				dsR.setFarmCode(farms.get(animal.getLocatedAtFarm()).getFarmID());
				dsR.setFarmWKT(farms.get(animal.getLocatedAtFarm()).getGeoLocation());
				uID = uID + "-" + dsR.getFarmCode();
			}
			dsR.setId(uID + "-" + animal.getAnimalID());
			observations.forEach((obsId, obs) -> {
				if (obs.getHasFeatureOfInterest().equals(id)) {
					
					if(obs.getOrder().equals("1")) {
						dsR.setM1(obs.getHasSimpleResult());
						dsR.setM1Date(obs.getResultTime());
						dsR.setId(dsR.getId() + "-" + "1");
					} else {
						dsR.setM2(obs.getHasSimpleResult());
						dsR.setM2Date(obs.getResultTime());
						dsR.setId(dsR.getId() + "-" + "2");
					}
				}
			});
			dsRList.add(dsR);
		});
		
		/*Exporting Records to XML KA format*/
		for(UC2DatasetRecord dsR:dsRList) {
			rows += "<ROW ID=\"" + dsR.getId()
					+ "\" AnimalGroupName=\"" + dsR.getAnimalGroupName()
					+ "\" AnimalGroupType=\"" + dsR.getAnimalGroupType()
					+ "\" AnimalGroupDate=\"" + dsR.getAnimalGroupDate()
					+ "\" FarmName=\"" + dsR.getFarmName()
					+ "\" FarmDescription=\"" + dsR.getFarmDescription()
					+ "\" FarmAddress=\"" + dsR.getFarmAddress()
					+ "\" FarmWKT=\"" + dsR.getFarmWKT()
					+ "\" FarmCode=\"" + dsR.getFarmCode()
					+ "\" EarTag=\"" + dsR.getEarTag()
					+ "\" Breed=\"" + dsR.getBreed()
					+ "\" Sex=\"" + dsR.getSex()
					+ "\" Born=\"" + dsR.getBorn()
					+ "\" M1=\"" + dsR.getM1()
					+ "\" M1Date=\"" + dsR.getM1Date()
					+ "\" M2=\"" + dsR.getM2()
					+ "\" M2Date=\"" + dsR.getM2Date()
					+ "\" Pos=\"" + dsR.getPos()
					+ "\" Extra=\"" + dsR.getExtra()
					+ "\" Comments=\"" + dsR.getComments()
					+ "\" Picture=\"" + dsR.getPicture()
					+ "\"/>";
		}
		
		rows += "</ROWS>";
		
		//rows = rows.replaceAll("Ñ","&#209;");
		//System.out.println("XML DATASET: ["+rows+"]");
		return rows;
	}
	
	public String debugTest(StringBuilder aim) throws JSONException, Exception {
		return aimTranslator(aim);
	  }
}
