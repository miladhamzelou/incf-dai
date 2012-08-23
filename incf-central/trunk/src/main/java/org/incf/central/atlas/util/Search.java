package org.incf.central.atlas.util; 

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Search {

/*    private static final String START = "C";
    private static final String END = "E";
*/
    public static String START = "Mouse_Paxinos_1.0";
    public static String END = "Mouse_WHS_0.9";


    public static void main(String[] args) {
        // this graph is directional

    	Graph graph = new Graph();

        //Get all the transformation chain data
	    CentralServiceDAOImpl impl = new CentralServiceDAOImpl();
	    CentralServiceVO vo1 = new CentralServiceVO();
		ArrayList dataList = impl.getCentralSpaceTransformationData(vo1);

		Iterator iterator = dataList.iterator();
        while (iterator.hasNext()) {
        	vo1 = (CentralServiceVO) iterator.next();
        	graph.addEdge(vo1.getTransformationSource(), vo1.getTransformationDestination());
        }

        //Retreive the matching chain for Source and Destination
        LinkedList<String> visited = new LinkedList();
        visited.add(START);
        ArrayList chainList = new ArrayList();
        chainList = new Search().breadthFirst(graph, visited, chainList);
        System.out.println("Chain List size is: " + chainList.size());

        //Arrange the chain result into pairs
        ArrayList transformationChainList = new Search().getTransformationChainlist(chainList, dataList);
        
        Iterator transformationChainListIter = transformationChainList.iterator();
        String transformationChainString;
        while ( transformationChainListIter.hasNext() ) {
        	transformationChainString = (String) transformationChainListIter.next();
        	System.out.println("TransformationChainList is: " + transformationChainString);
        }

    }


    public ArrayList getCoordinateTransformationChainList(CentralServiceVO vo) {
    	
    	this.START = vo.getFromSRSCode();
    	this.END = vo.getToSRSCode();

    	System.out.println("Start - " + this.START);
    	System.out.println("End - " + this.END);
    	
    	Graph graph = new Graph();

        //Get all the transformation chain data
	    CentralServiceDAOImpl impl = new CentralServiceDAOImpl();
	    CentralServiceVO vo1 = new CentralServiceVO();
		ArrayList dataList = impl.getCentralSpaceTransformationData(vo1);

		Iterator iterator = dataList.iterator();
        while (iterator.hasNext()) {
        	vo1 = (CentralServiceVO) iterator.next();
        	graph.addEdge(vo1.getTransformationSource(), vo1.getTransformationDestination());
        }

        //Retreive the matching chain for Source and Destination
        LinkedList<String> visited = new LinkedList();
        visited.add(START);
        ArrayList chainList = new ArrayList();
        chainList = new Search().breadthFirst(graph, visited, chainList);
        System.out.println("Chain List size is: " + chainList.size());

        //Arrange the chain result into pairs
        ArrayList transformationChainList = new Search().getTransformationChainlist(chainList, dataList);
        
        return transformationChainList;

    }
    
    public ArrayList getTransformationChainlist(ArrayList chainList, ArrayList dataList){ 
        Iterator chainIter = chainList.iterator();
        String newSRSName = "";
        String oldSRSName = "empty";
        String srsPair = "";
        ArrayList pairList = new ArrayList();
        while (chainIter.hasNext()) {
        	newSRSName = (String)chainIter.next();
        	srsPair = oldSRSName+":"+newSRSName;
        	oldSRSName = newSRSName;
        	pairList.add(srsPair);
        	//System.out.println("srsPair: " + srsPair);
        }
        pairList.remove(0);
        //System.out.println("Size is: " + pairList.size());
        
        //Assign the hub 
        Iterator pairlistIter = pairList.iterator();
        String source = "";
        String destination = "";
        srsPair = "";
        
        ArrayList transformationChainList = new ArrayList();
        String transformationChainString = "";
        
        
        while (pairlistIter.hasNext()) { 
        	srsPair = (String) pairlistIter.next();
	        StringTokenizer tokens = new StringTokenizer(srsPair, ":");
	        while (tokens.hasMoreTokens()) {
	        	source = tokens.nextToken();
	        	destination = tokens.nextToken();
	        }

	        Iterator dataListIter = dataList.iterator();
	        CentralServiceVO dataListVO;

	        while ( dataListIter.hasNext() ) {
	        	dataListVO = (CentralServiceVO) dataListIter.next();
				//System.out.println("Source in chain: " + source + " and Destination in chain: " + destination);
				//System.out.println("Source in list: " + dataListVO.getTransformationSource()+ " and Destination in list: " + dataListVO.getTransformationDestination());
	        	if ( dataListVO.getTransformationSource().equalsIgnoreCase(source) && 
	        		 dataListVO.getTransformationDestination().equalsIgnoreCase(destination) ) {

	        		transformationChainString = dataListVO.getTransformationSource()+":"+dataListVO.getTransformationDestination()+":"+
	        		dataListVO.getTransformationHub();
	        		//System.out.println("Hub is here" + dataListVO.getTransformationHub());

	        	}
	        }
    		transformationChainList.add(transformationChainString);
        }

        return transformationChainList;
    }

    private ArrayList breadthFirst(Graph graph, LinkedList<String> visited, ArrayList list) {

    	LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
    	// examine adjacent nodes
        for (String node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(END)) {
                visited.add(node);
                printPath(visited, list);
                visited.removeLast();
                break;
            }
        }
        // in breadth-first, recursion needs to come after visiting adjacent nodes
        for (String node : nodes) {
            if (visited.contains(node) || node.equals(END)) {
                continue;
            }
            visited.addLast(node);
            breadthFirst(graph, visited, list);
            visited.removeLast();

        }

        return list;

    }

    private void printPath(LinkedList<String> visited, ArrayList list) {

    	for (String node : visited) {
            //System.out.print(node);
            //System.out.print(" ");
            list.add(node);
        }
    }
}