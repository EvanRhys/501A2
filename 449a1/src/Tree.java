/***************
 * CPSC 449 Assignment 1 
 * Tree Class
 * @author team 1-1 
 * contains recursive method searchR that does a DFS of all pairs of machines and tasks
 * then sets bestScore as the minimal penalty score
 * also sets bestString as order of tasks that give the minimal score. 
 * First character in the machine is paired with the first machine, and so on
 * 
 * version 2
 * searchR changed, toonearpen2
 */

public class Tree {
	//final Score
	private int bestScore;
	//final String of pairs: ie computer 1 = task letter at String position 0
	private String bestString;	
	private HardConstraints HC;
	private SoftConstraints SC;
	public Tree(){
		bestScore = -1;
		bestString = "No valid solution possible!";
	}
	/*******
	 * @param HC - Hard Constraints class containing forbidden machine, forced partial assignment, and too-near tasks data structures
	 * @param SC - Soft constraints class containing too-near penalties and machine penalties data structures  
	 */
	public Tree(HardConstraints HC, SoftConstraints SC){
		bestScore = -1;
		bestString = "No valid solution possible!";
		this.HC = HC;
		this.SC = SC;		
	}
	/*******
	 * @param remain - String containing tasks that have not been assigned to a machine 
	 * @param curr - String containing current list of tasks
	 * @param mNumb - keeps track of which machine number the task is being added to
	 * @param pen - current running penalty count
	 * @param last - task assigned to machine i - 1
	 * 
	 * loops through the remaining string at each level of the recursive call
	 * first checks if there is a forced partial assignment, HC will return 'X' if there isn't
	 * 	if the remaining string does not contain the task trying to force it will not add it, returning false
	 * if not force partial assignment, take the next task and check if it is valid to add
	 * 	then get the penalty score and check if there it is greater then the best score
	 *  if either fail the count is increased and the loop will continue with the next task
	 * when the next recursive call is made a new remain, curr, mNumb, pen, and last are passed
	 * 	this insures that when the recursive call is closed the parent still has the same values to work with
	 * finally when the remaining string is empty the score is checked one last time against the best and if better it is saved
	 */
	public boolean searchR(String remain, String curr, int mNumb, int pen, char last){
		String temp [];
		String nextR;
		String nextC;
		int nextP;
		int count = 0;
		char task;
		char nextL;
		CharSequence seq;
		if (remain.length() != 0){
			while(count < remain.length()){
				if ((task = HC.forcePartAssignment(mNumb)) != 'X'){
					if (HC.constraint(mNumb, task, last)){
						seq = Character.toString(task);
						if (remain.contains(seq)){
							nextC = curr + task;
							temp = remain.split(Character.toString(task));
							if (temp.length > 1){
								nextR = temp[0] + temp[1];
							}
							else if (temp.length == 1) {
								nextR = temp[0];
							}
							else
								nextR = "";		
							nextP = pen + SC.getPenalty(mNumb, task, last);
							nextL = task;
							searchR(nextR, nextC, mNumb + 1, nextP, nextL);
						}
						count = remain.length();
					}
					else
						return false;
				}
				else{
					task =  remain.charAt(count);
					if (HC.constraint(mNumb, task, last)){
						nextP = pen + SC.getPenalty(mNumb, task, last);
						if ((pen < bestScore) || (bestScore == -1)){
							nextC = curr + task;
							nextL = task;
							temp = remain.split(Character.toString(task));
							if (temp.length > 1){
								nextR = temp[0] + temp[1];
							}
							else if (temp.length == 1) {
								nextR = temp[0];
							}
							else
								nextR = "";
							searchR(nextR, nextC, mNumb + 1, nextP, nextL);
							count++;
						}
						else
							count++;
					}
					else
						count++;
				}
			}
		}
		else{
			pen = pen + SC.getTooNearPenalty(curr.charAt(7), curr.charAt(0));//added to check if last element is next to first, toonearpen2
			if ((pen < bestScore) || (bestScore == -1)){
				bestScore = pen;
				bestString = curr;
			}
		}	
		return true;
	}
	/*******
	 * @return the value of the bestScore
	 */
	public int getBestScore() {
		return bestScore;
	}
	/*******
	 * @return the best task list
	 */
	public String getBestString() {
		return bestString;
	}
}
