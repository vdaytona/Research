package commonTool;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


/**
 * utility　of K-mean 
 * step 1: input the number N of group needed to be divided
 * (default N = 5) 
 * step 2: select random N center element from the rawData 
 * step 3: calculate the distance from other elements to the N center element (use
 * multithread) 
 * step 4: distribute the element to the group with shortest
 * distance (use multithread) 
 * step 5: calculate the average value of each group,
 * and choose the average point as the new center (use multithread) 
 * step 6:
 * repeat the step from 3 to 5, until the distribution of former groups are the
 * same as that of the present groups
 * 
 * @author mingjie.ding
 * @param <T>
 * 
 */

public class ClusteringKmeans<T> {
	
	private Integer groupNumber;  //Separate into N group
	private HashMap<String, ArrayList<T>> vectorMap; //container for input data
	private Integer vectorDimension; //dimension of the input data
	private HashMap<Integer, ArrayList<Double>> centerMap; //key : center point ID, value : the coordinates of the point
	private HashMap<Integer, ArrayList<String>> newGroupMap; //the group list for the new group, key : group Id, value : group list
	private HashMap<Integer, ArrayList<String>> oldGroupMap; //the group list for the old group, key : group Id, value : group list
	private HashMap<String, ArrayList<Double>> distanceMap; //the distance list of each input element to the group center
	private Boolean finishFlag = false; //flag of if the clustering is finished

	public ClusteringKmeans() {
		vectorMap = new HashMap<>();
		centerMap = new HashMap<>();
		newGroupMap = new HashMap<>();
		oldGroupMap = new HashMap<>();
		distanceMap = new HashMap<>();
		vectorDimension = null;
		groupNumber = 2;
	}
	
	/**
	 * entrance
	 * @param inputRawData
	 * @return
	 */
	public HashMap<Integer, ArrayList<String>> clusteringByKmean(HashMap<String,ArrayList<T>> inputRawData, Integer groupNumber) {
		
		this.groupNumber = groupNumber;
		
		for (String key : inputRawData.keySet()) {
			vectorMap.put(key, inputRawData.get(key));
			if (vectorDimension == null) {
				vectorDimension = inputRawData.get(key).size();
			}
			
		}
		
		
		// step 1: input the number N of group needed to be divided (default N
		// = 2)
		this.groupNumber = groupNumber;
		
		// step 2.select random N center element from the rawData
		initialCenterMap(inputRawData);
		

		while (finishFlag == false) {
			// step 3: calculate the distance from other elements to the N
			// center
			// element (use multithread)
			calculateGroupDistance();

			// step 4: distribute the element to the group with shortest
			// distance (use multithread)
			arrangeGroup();

			// step 5: compare newgroup to oldgroup, if same finish, if
			// different redo from step 3
			compareOldAndNewGroup();
			if (finishFlag == false) {
				calcualteCenterPointMap();
			}
		}

		return newGroupMap ;
	}

	/**
	 *  find random points as the first center point
	 * @param inputRawData
	 */
	public void initialCenterMap(HashMap<String,ArrayList<T>> inputRawData) {
		Integer count = 0;
		for (String key : inputRawData.keySet()) {
			ArrayList<Double> vector = new ArrayList<>();
			for (int i = 0; i < vectorDimension ; i ++) {
				vector.add(Double.valueOf(inputRawData.get(key).get(i).toString()));
			}
			centerMap.put(count, vector);
			if (++count >= this.groupNumber) {
				break;
			}
		}
	}
	
	/**
	 * calculate the distance with the center point
	 */
	public void calculateGroupDistance() {
		distanceMap.clear();
		for (String key : vectorMap.keySet()) {
			ArrayList<Double> distanceList = new ArrayList<>();
			for (Integer i = 0; i < groupNumber; i++) {
				distanceList.add(calculateDistance(vectorMap.get(key),
						centerMap.get(i)));
			}
			distanceMap.put(key, distanceList);
		}
	}

	/**
	 * calculate distance between two points
	 * @param o1
	 * @param arrayList
	 * @return
	 */
	public Double calculateDistance(ArrayList<T> o1, ArrayList<Double> arrayList) {
		Double sumSquare = 0.0;
		for (int i = 0; i < o1.size(); i++) {
			sumSquare += Math.pow(
					Double.valueOf(o1.get(i).toString())
							- Double.valueOf(arrayList.get(i).toString()), 2);
			
		}
		return Math.pow(sumSquare, 0.5);
	}

	/**
	 * put the point into the group with the cloest distance with the group center point
	 */
	public void arrangeGroup() {
		
		for (Integer key : newGroupMap.keySet()) {
			oldGroupMap.put(key, newGroupMap.get(key));
		}
		newGroupMap = new HashMap<>();
		
		for (String key : distanceMap.keySet()) {
			Integer minIndex = findMinIndex(distanceMap.get(key));
			if (newGroupMap.containsKey(minIndex)) {
				newGroupMap.get(minIndex).add(key);
			} else {
				ArrayList<String> newList = new ArrayList<>();
				newList.add(key);
				newGroupMap.put(minIndex, newList);
			}
		}
	}

	/**
	 * find the minValue index in input data list
	 * @param inputList
	 * @return
	 */
	public Integer findMinIndex(ArrayList<Double> inputList) {
		ArrayList<Double> sortList = new ArrayList<>(inputList);
		
		Collections.sort(sortList, new Comparator<Double>() {
			public int compare(Double o1, Double o2) {
				return o1.compareTo(o2);
			}
		});
		
		return inputList.indexOf(sortList.get(0));
	}

	/**
	 * identify if the newgroup is the same as the oldgroup, if they are the same, jump out of the calculate loop
	 */
	public void compareOldAndNewGroup() {
		Boolean tempFlag = true;
		if (oldGroupMap.keySet().size() == 0) {
			tempFlag = false;
		}
		for (Integer groupNumber : oldGroupMap.keySet()) {
			for (String customerId : oldGroupMap.get(groupNumber)) {
				if (!newGroupMap.get(groupNumber).contains(customerId)) {
					tempFlag = false;
					break;
				}
			}
			if (tempFlag == false) {
				break;
			}
		}
		this.finishFlag = tempFlag;
	}
	
	/**
	 * find the new group center point by calculating the average value of the each group
	 */
	public void calcualteCenterPointMap(){
		centerMap = new HashMap<>();
		for (Integer groupId : newGroupMap.keySet()) {
			ArrayList<Double> centerPoint = new ArrayList<>();
			for (int i = 0; i < vectorDimension ; i ++) {
				centerPoint.add(0.0);
			}
 			for (String key : newGroupMap.get(groupId)) {
				for (int i = 0; i < vectorDimension ; i ++) {
					centerPoint.set(i,centerPoint.get(i) + Double.valueOf(vectorMap.get(key).get(i).toString()));
				}
			}
 			for (int i = 0; i < vectorDimension ; i ++) {
				centerPoint.set(i, centerPoint.get(i) / Double.valueOf(newGroupMap.get(groupId).size()));
			}
 			centerMap.put(groupId,centerPoint);
		}
	}
}
