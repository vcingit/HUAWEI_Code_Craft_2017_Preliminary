package com.cacheserverdeploy.deploy;
import java.util.Random;

public class Individuals{
	private int save=0;//存活时间
	private int Id=-1;//个体的序号
	private int NodeNum;//网络节点数量
	private int ConsumeNum;//消费结点数量
	private int rate=100;//变异率
	private int[] gene;
	/*基因X,由0、1组成
	 *0：该网络结点不部署服务器
	 *1：该网络结点部署服务器
	 */
	private int[][] hid_user;//隐藏的副本用户
	private int[][] user_info;//副本用户
	
	public void Copy(Individuals cp){
		setGene(cp.getGene());
	}
	
	public Individuals(int NodeNum,int[][] user_info,int id){
		
		this.Id=id;
		this.setNodeNum(NodeNum);
		this.setConsumeNum(user_info.length);
		this.gene=new int[NodeNum];
		this.user_info=new int[ConsumeNum][2];

		this.setUser_info(user_info);
		
		hid_user=this.getUser_info();
	}

	public void ConstantBorn(int[] min_score,int SPEND,double B){//固定出生为直连服务器
			for(int j=0;j<ConsumeNum;j++){
				int i =user_info[j][0];
				//if(min_score[j]>=SPEND*B)
				{
					gene[i]=1;
				}
			}
		update();
	}
	
	public void RandomBorn(int[][] nei,int[][] info,int SPEND,double A,double B){
		/*条件
		 * 1.设安放服务器的网路节点基因段为1，否则为0
		 * 2.安放服务器的节点数不能超过消费节点数
		 * 
		 * info[][0]:节点id
		 * info[][1]:邻边数
		 * info[][2]:临近总流量
		 * info[][3]:临近用户数
		 * info[][4]:是否直连用户
		 * info[][5]:非直连用户的成本
		 * info[][6]:是否是服务器
		 * info[][7]:临近总权值
		 * info[][8]:二级相邻用户
		 */
		restore();
		Random r=new Random();
		int rates=(int)(ConsumeNum*A);
		int[] loc=new int[500];//可能建设服务器的位置
		int num=0;
		if(NodeNum<300){
			
			for(int i=0;i<info.length;i++){
				if(info[i][4]==1&&(info[i][1]>=10||info[i][2]>300||info[i][3]>=5)){//用户直连且邻边较多或流量很大
					gene[i]=1;
					rates--;
				}
				else if(info[i][5]>(int)(SPEND*B)) {//非直连成本超过限度
					//count++;
					gene[i]=1;
					rates--;
				}

			}
			
			for(int i=0;i<rates;i++){
				//gene[r.nextInt(NodeNum)]=1;
				gene[user_info[r.nextInt(ConsumeNum)][0]]=1;
			}	
		}
		else if(NodeNum<600){
			for(int i=0;i<NodeNum;i++){
				if(info[i][2]<100||info[i][5]<100){//总流量小于100，或非建站成本低不建站
					gene[i]=0;
				}
				else if(info[i][4]==0&&info[i][1]<15){//不直连用户且邻边较少，直接不考虑建站
					gene[i]=0;
				}
				else if(info[i][5]>(int)(SPEND*B)){//非建站成本高，直接建站
					gene[i]=1;
					rates--;
				}
				else{
					loc[num++]=i;
				}
			}
			
			for(int i=0;i<rates;i++){
				//gene[user_info[r.nextInt(ConsumeNum)][0]]=1;
				gene[loc[r.nextInt(num)]]=1;
			}		
		}
		else{

			for(int i=0;i<NodeNum;i++){
				if(info[i][5]>(int)(SPEND*B)){//非建站成本高，直接建站
					gene[i]=1;
					rates--;
				}
				else if((double)info[i][2]/(double)info[i][7]<4.0&&info[i][8]<30){//性价比低,且与用户距离远
					gene[i]=0;
				}
				else if(info[i][4]==0&&(info[i][1]<15||info[i][2]<80)){//不直连用户且邻边较少或总流量较少，直接不考虑建站
					gene[i]=0;
				}
				else{
					loc[num++]=i;
				}
			}
			
			for(int i=0;i<rates;i++){
				//gene[r.nextInt(NodeNum)]=1;
				int flag=0;
				int dd=loc[r.nextInt(num)];
				for(int j=0;j<nei[dd][nei[dd].length-1];j++){
					if(gene[nei[dd][j]]==1){
						i--;
						flag=1;
						break;
					}
				}
				
			}
		}
		update();
	}

	public void Varition(){//变异操作
		this.restore();//先恢复用户需求表
		int count=CountGene();//获取服务器数
		Random r=new Random();//随机变异
		int[] loc=new int[count];//保存服务器位置
		int[] noloc=new int[NodeNum-count];//非服务器位置
		int c=0,d=0;
		for(int i=0;i<NodeNum;i++){
			if(gene[i]==1)loc[c++]=i;
			else noloc[d++]=i;
		}
		int p=r.nextInt(100);
		if(p<getRate()){
			int q=r.nextInt(100);
			//if(q<40&&count<ConsumeNum){//扩建变异
			if(q<50&&noloc.length!=0){
				int index=r.nextInt(noloc.length);
				gene[noloc[index]]=1;
			}
			else if(loc.length>0){//撤建变异
				int index=r.nextInt(loc.length);
				gene[loc[index]]=0;
			}
		}
		this.update();
	}
	
	public int MatchGene(){
		int count=0;
		for(int i=0;i<NodeNum;i++){
			for(int j=0;j<ConsumeNum;j++){
				if(user_info[j][0]==i&&gene[i]==1){
					count++;
				}
			}
		}
		return count;
	}
	
	public void Print(String flag){
		/* case0:打印cap
		 * case1:打印cost
		 * case2:打印user_info
		 * case3:打印gene
		 * 
		 */
		System.out.println("Print Individuals~"+Id+":");
//		if(flag.equals("cap")){
//			System.out.println("Print Cap:");
//			for(int i=0;i<cap.length;i++){
//				System.out.print("Node:"+i+"->");
//				for(int j=0;j<cap[i].length;j++){
//					if(cap[i][j]!=0)System.out.print(j+":"+cap[i][j]+" ");
//				}
//				System.out.println();
//			}
//		}
//		else if(flag.equals("cost")){
//			System.out.println("Print Cost:");
//			for(int i=0;i<cost.length;i++){
//				System.out.print("Node:"+i+"->");
//				for(int j=i+1;j<cost[i].length;j++){
//					System.out.print(j+":"+cost[i][j]+" ");
//				}
//				System.out.println();
//			}
//		}
		if(flag.equals("user")){
			System.out.println("Print User_Info:");
			for(int i=0;i<user_info.length;i++){
				System.out.print("User:"+i+" ");
				for(int j=0;j<user_info[i].length;j++){
					System.out.print(user_info[i][j]+" ");
				}
				System.out.println();
			}
		}
		else if(flag.equals("gene")){
			int[] tmp=new int[CountGene()];
			int c=0;
			System.out.println("Print Gene:");
			for(int i=0;i<gene.length;i++){
				if(gene[i]!=0) tmp[c++]=i;
				System.out.print(gene[i]);
			}
			System.out.println();
			System.out.print("LOC:");
			for(int i:tmp)System.out.print(i+" ");		
			System.out.println("("+CountGene()+" count "+MatchGene()+" match)");
		}
		else{
			System.out.println("NULL PRINT!");;
		}
	}
	
	public void restore(){//恢复user表
		for(int i=0;i<user_info.length;i++)
		{
			user_info[i][0]=hid_user[i][0];
			user_info[i][1]=hid_user[i][1];
		}
	}

	public void update(){
		//如果用户直连的结点上有服务器，用户需求改为0
		for(int j=0;j<ConsumeNum;j++){
			if(gene[user_info[j][0]]==1){
				user_info[j][1]=0;
			}
		}
	}
	
	public int CountGene(){
		int count=0;
		for(int i=0;i<gene.length;i++){
			if(gene[i]==1)count++;
		}
		return count;
	}
	
	public int getConsumeNum() {
		return ConsumeNum;
	}
	
	public void setConsumeNum(int consumeNum) {
		ConsumeNum = consumeNum;
	}
	
	public int getNodeNum() {
		return NodeNum;
	}
	
	public void setNodeNum(int nodeNum) {
		NodeNum = nodeNum;
	}

	public int[] getGene() {
		int[] gene=new int[this.gene.length];
		for(int i=0;i<gene.length;i++)
			gene[i]=this.gene[i];
		return gene;
	}

	public void setGene(int[] gene) {
		restore();
		for(int i=0;i<gene.length;i++)
			this.gene[i]=gene[i];
		update();
	}

	public int[][] getUser_info() {
		int[][] user_info=new int[this.user_info.length][this.user_info[0].length];
		for(int i=0;i<user_info.length;i++)
			for(int j=0;j<user_info[0].length;j++)
				user_info[i][j]=this.user_info[i][j];
		return user_info;
	}
	
	public int[][] getHid_user() {
		int[][] hid_user=new int[this.hid_user.length][this.hid_user[0].length];
		for(int i=0;i<hid_user.length;i++)
			for(int j=0;j<hid_user[0].length;j++)
				hid_user[i][j]=this.hid_user[i][j];
		return hid_user;
	}

	public void setUser_info(int[][] user_info) {
		for(int i=0;i<user_info.length;i++)
			for(int j=0;j<user_info[0].length;j++)
				this.user_info[i][j]=user_info[i][j];
		update();
	}

//	public int[][] getCap() {
//		int[][] cap=new int[this.cap.length][this.cap[0].length];
//		for(int i=0;i<cap.length;i++)
//			for(int j=0;j<cap[0].length;j++)
//				cap[i][j]=this.cap[i][j];
//		return cap;
//	}
//
//	public void setCap(int[][] cap) {
//		for(int i=0;i<cap.length;i++)
//			for(int j=0;j<cap[0].length;j++)
//				this.cap[i][j]=cap[i][j];
//	}

//	public int[][] getCost() {
//		int[][] cost=new int[this.cost.length][this.cost[0].length];
//		for(int i=0;i<cost.length;i++)
//			for(int j=0;j<cost[0].length;j++)
//				cost[i][j]=this.cost[i][j];
//		return cost;
//	}
//
//	public void setCost(int[][] cost) {
//		for(int i=0;i<cost.length;i++)
//			for(int j=0;j<cost[0].length;j++)
//				this.cost[i][j]=cost[i][j];
//	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getSave() {
		return save;
	}

	public void setSave(int save) {
		this.save = save;
	}
}