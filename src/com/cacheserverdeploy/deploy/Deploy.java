package com.cacheserverdeploy.deploy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class Deploy {
    /**
     * 你需要完成的入口 <功能详细描述>
     * 
     * @param graphContent
     *            用例信息文件
     * @return [参数说明] 输出结果信息
     * @see [类、类#方法、类#成员]
     */
	
    private static Random r = new Random();
    private static int[][] node_relation;// 保存节点临边关系的数组
    private static int[][] cap;// 流量地图
    private static int[][] hid_cap;// 隐藏流量地图
    private static int[][] cost;// 单价地图
    private static int[][] user_info;// 消费结点信息
    private static int[][] cap_info;
    private static int[] min_score;// 用户不部署服务器的最低消耗
    private static int[][] min_cost_max_flux;// 最小费用最大流数组
    private static int max_num_of_edge = 800;// 所有点临边不超过20
    private static int max_num_of_node = 1005;// 点不会超过1000个
    private static int max_num_of_road = 5000;// 结果路径最多5000条
    public static int POP_SIZE = 16;// 种群数量
    public static int Gap = 500;// 迭代次数
    public static int INT_MAX = 1000000;// 路不通时的单价
    public static int FLUX_MIN = 0;// 路不通时的带宽
    public static int MAX_TIME = 87000;// 程序运行的最大时间80s
    public static long START_TIME = System.currentTimeMillis();// 程序开始时间
    public static int RUNTIMES = 0;// 迭代次数
    public static int SPEND = 0;// 每台服务器花费
    public static int VARITION_RATE = 10;// 变异率
    public static int MIN_VA_RATE = 5;// 最小变异率
    public static int MAX_VA_RATE = 30;// 最大变异率
    public static int CHOOSE_RATE = 10;// 优胜率
    public static int MAX_MAT_RATE = 90;// 最大交叉率
    public static int MIN_MAT_RATE = 50;// 最小交叉率
    public static int MAT_RATE = 90;// 交叉率
    public static double inc_rate = 0.05;//增长率
    public static double dec_rate = 0.04;//下降率
    public static double A = 0.8;// A参数,服务器/用户的比例
    public static double B = 0.5;// B参数,非建站最小花费
    public static int D = 1;// 每D轮输出结果
    public static int C = 0;// 至少在C轮后输出结果
/*
 * 待实现的最小费用最大流，存在问题
 * 
    private static int MAXN;
    private static int inf;  
    private static int[] pre;          // pre[v] = k：在增广路上，到达点v的边的编号为k  
    private static int[] dis;          // dis[u] = d：从起点s到点u的路径长为d  
    private static boolean[] vis;         // inq[u]：点u是否在队列中  
    private static int[] path;  
    private static int[] head;  
    public static int NE,ans,max_flow;
    private static int[][] Edge;//u,v,cap,cost,next
    private static int[][] tmp_cap;
    private static int[][] tmp_cost;
    private static int[] tmp_head;
    private static int[] stack;
    private static int road_num=0;
    private static int[][] tmp_node_relation;
    private static int[][] tmp_Edge;
    private static int[] tmp_node;
    private static int[] tmp_visit;
    public static void addEdge(int[][] Edge,int[] head,int u,int v,int cap,int cost)  
    {  
        Edge[NE][0]=u;  
        Edge[NE][1]=v;  
        Edge[NE][2]=cap;  
        Edge[NE][3]=cost;  
        Edge[NE][4]=head[u];  
        head[u]=NE++;  
        Edge[NE][0]=v;  
        Edge[NE][1]=u;  
        Edge[NE][2]=0;  
        Edge[NE][3]=-cost;  
        Edge[NE][4]=head[v];  
        head[v]=NE++;  
    }  
    public static boolean SPFA(int[][] Edge,int[] head,int s,int t)   //  源点为0，汇点为sink。  
    {  
        int i;  
        for(i=0;i<MAXN;i++) dis[i]=inf;  
        for(i=0;i<MAXN;i++) vis[i]=false; 
        for(i=0;i<MAXN;i++) pre[i]=-1; 
        dis[s] = 0; 
        
        int[] q = new int[MAXN + 10];// 队列
        int front = 0;// 队列头
        int tail = 0;// 队列尾  
        q[tail++]=s; //起点 
        vis[s] =true;  //标记已访问
        while(front!=tail)        //  这里最好用队列，有广搜的意思，堆栈像深搜。  
        {  
        	
            int u =q[front++]; 
            front = front == MAXN + 10 ? 0 : front;
            for(i=head[u]; i!=-1;i=Edge[i][4])  
            {  //System.out.println(1);
                int v=Edge[i][1];  
                //System.out.println("i:"+Edge[i][0]+" "+Edge[i][1]+" "+Edge[i][2]+" "+Edge[i][3]+"+"+dis[u]+" "+dis[v]);
                if(Edge[i][2] >0&& dis[v]>dis[u]+Edge[i][3])  
                {  
                    dis[v] = dis[u] + Edge[i][3];  
                    pre[v] = u;  
                    path[v]=i;  
                    if(!vis[v])  
                    {  
                    	q[tail++] = v;
                        tail = tail == MAXN + 10 ? 0 : tail;
                        vis[v] = true; 
                    }  
                }  
            }  
            vis[u] =false;  
        }  //System.out.println("pre:"+pre[t]);
        if(pre[t]==-1)  
            return false;  
        return true;  
    }  
    public static void end(int[][] Edge,int s,int t)  
    {  
        int u, sum = inf;  
        for(u=t; u!=s; u=pre[u])  
        {  
            sum = Math.min(sum,Edge[path[u]][2]);  
        }  
        max_flow+=sum;                          //记录最大流  
        for(u = t; u != s; u=pre[u])  
        {  
        	System.out.print(Edge[path[u]][1]+"<-");
            Edge[path[u]][2] -= sum;  
            Edge[path[u]^1][2] += sum;  
            ans += Edge[path[u]][3];     //  cost记录的为单位流量费用，必须得乘以流量。  
        } 
        System.out.println(Edge[path[s]][0]);
    } 
    public static void FindRoad(int s,int t){
        ans=max_flow=0;    
        while(SPFA(tmp_Edge,tmp_head,s,t)){
        	end(tmp_Edge,s,t);  
        }
        System.out.println("ans:"+ans+" flux:"+max_flow);
        
        //DFS寻路
        for(int i=1;i<NE;i+=2){
        	int start=tmp_Edge[i][1];
        	int end=tmp_Edge[i][0];
        	int flux=tmp_Edge[i][2];
        	tmp_cap[start][end]=flux;
        	if(start==s)tmp_node[start]+=flux;
        }
        
        road_num=0;
        stack[0]=s;
        for(int i=0;i<MAXN;i++){
        	tmp_visit[i]=0;
        }
        DFS(tmp_cap,tmp_node,min_cost_max_flux,stack,s,t,inf,0,1);
        
        int[][] user_infos=new int[user_info.length][2];
        CopyArr(user_infos,user_info);
        
        for(int i=0;i<road_num;i++){
        	int len=min_cost_max_flux[i][min_cost_max_flux[i].length-1];
        	for(int j=0;j<len;j++){

        		System.out.print(min_cost_max_flux[i][j]+" ");
        		if(j==len-2){
        			for(int k=0;k<user_info.length;k++){
        				if(user_info[k][0]==min_cost_max_flux[i][j]){
        					System.out.print(k+" ");
        					user_infos[k][1]-=min_cost_max_flux[i][j];
        					break;
        				}
        			}
        		}
        	}
        	System.out.println();
        }
        	
        
    }
    public static void DFS(int[][] tmp_cap,int[] tmp_node,int[][] res_path,int[] stack,int s,int t,int min,int sp,int count){
    	tmp_visit[s]=1;

    	if(s==t){//到达终点
    		//System.out.println(road_num);
    		res_path[road_num][0]=min;//第一位保存流量
    		res_path[road_num][1]=sp;//第二位保存花费
    		//System.out.println("min:"+min);
    		for(int i=0;i<count;i++){//整条路径
    			res_path[road_num][i+2]=stack[i];//保存路径
    		}
    		//System.out.println();
    		res_path[road_num][res_path[road_num].length-1]=count+2;//最后一位保存数组长度
    		road_num++;//路径数+1
    		res_path[max_num_of_edge][max_num_of_node-1]=road_num;
    	}
    	else{//没到终点
    		int len=tmp_node_relation[s][tmp_node_relation[s].length-1];
    		for(int i=0;i<len;i++){//深度搜索
    			if(tmp_node[s]==0)
    				break;
    			int id=tmp_node_relation[s][i];//节点id
    			if(tmp_cap[s][id]!=0&&tmp_visit[id]!=1){//还有流量可分配给这个节点
    				min=Math.min(min, tmp_cap[s][id]);
    				min=Math.min(min, tmp_node[s]);
    				//System.out.print(s+"("+tmp_node[s]+")-["+min+"]->"+id+"("+tmp_node[id]+") => ");
    				tmp_node[id]+=min;
    				tmp_node[s]-=min;
    				//System.out.println(s+"("+tmp_node[s]+")+"+id+"("+tmp_node[id]+")");
    				stack[count]=id;//此路径又记录了一个点
    				tmp_visit[id]=1;
    				DFS(tmp_cap,tmp_node,res_path,stack,id,t,min,sp+tmp_cost[s][id],count+1);//记录遍历这个点
    				tmp_visit[id]=0;
    			}
    		}
    	}
    }
    public static void SuperFlux(Individuals Ind){//建立超级源点超级汇点
    	
        CopyArr(tmp_node_relation,node_relation);
    	CopyArr(tmp_cost,cost);
    	
    	int p1=cap.length;
    	int p2=cap.length+1;
    	int[] genes=Ind.getGene();
    	int[][] user_infos=Ind.getUser_info();
    	
    	
    	for(int i=0;i<MAXN;i++)tmp_node[i]=0;
    	for(int i=0;i<MAXN;i++)tmp_head[i]=-1;
    	NE=0;
    	for(int i=0;i<cap_info.length;i++){
    		int start=cap_info[i][0];
    		int end=cap_info[i][1];
    		int flux=cap_info[i][2];
    		int cost=cap_info[i][3];
    		if(genes[end]!=1)
    			addEdge(tmp_Edge,tmp_head,start,end,flux,cost);
    		if(genes[start]!=1)
    			addEdge(tmp_Edge,tmp_head,end,start,flux,cost);
    	}
    	
    	for(int i=0;i<genes.length;i++){
    		if(genes[i]==1){//建立超级源点
    			AddRelation(tmp_node_relation,p1,i);
    			addEdge(tmp_Edge,tmp_head,p1,i,inf,0);
    	        //tmp_node[i]=inf;
    		}
    	}
//        
    	for(int i=0;i<user_infos.length;i++){//建立超级汇点
    		int id=user_infos[i][0];
    		AddRelation(tmp_node_relation,p2,id);
    	    addEdge(tmp_Edge,tmp_head,id,p2,inf,0);
    	}
  

    	//Ind.Print("gene");
        FindRoad(p1,p2);
    }
    public static void GetScore(Individuals Ind){
    	int spend = 0;// 选路成本
        int flag = 0;// 判断是否可以结束寻路
        int[][] user_infos = Ind.getUser_info();

        SuperFlux(Ind);
        spend += Ind.CountGene() * SPEND;// 计算服务器成本
        int num_of_road = min_cost_max_flux[max_num_of_road - 1][max_num_of_node - 1];// 总路数
        int now_loc = 0;// 记录当前第几条路
//        for(int i=0;i<num_of_road;i++){
//        	int len=min_cost_max_flux[i][min_cost_max_flux[i].length-1];
//        	for(int j=0;j<len;j++){
//        		System.out.print(min_cost_max_flux[i][j]+" ");
//        	}
//        	System.out.println();
//        }
    }
    public static void test(){
    	Individuals in=new Individuals(cap.length,user_info,1);
    	int[] genes={0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0};
    	in.setGene(genes);
    	System.out.println(FindBestScore(cap,in));
    	SuperFlux(in);
    }
*/    
    public static String[] deployServer(String[] graphContent) {

        /** do your work here **/
        InitMap(graphContent);
        PreOperation();
        String[] s = FirstResult();
//        test();
        if (cap.length < 200) {// 初级
            POP_SIZE = 40;
            Gap = 800;
            inc_rate=0.04;
            dec_rate=0.05;
            A=0.6;
            B=0.5;
            s = GeneticAlgorithm();
        } else if (cap.length < 500) {// 中级
        	POP_SIZE = 20;
            Gap = 600;
            inc_rate=0.05;
            dec_rate=0.03;
            A=0.35;
            B=0.5;
            s = GeneticAlgorithm();
        } else {// 高级
        	
            POP_SIZE = 10;
            Gap = 400;
            inc_rate=1;
            dec_rate=0.03;
            A=0.35;
            B=0.35;
            MAX_VA_RATE=60;
            s = GeneticAlgorithm();
        }
        return DealWithRes(s);
        /** do your work here **/
    }

    private static int[][] ppp(Individuals iii) {//获得所有节点的一些信息

    	int [][] rrr=new int[cap.length][10];
        iii.Print("gene");
        int[] ttt=iii.getGene();
        int[][] uuu=iii.getUser_info();
        
        int[] nnn=new int[ttt.length];//总流量
        int[] mmm=new int[ttt.length];//相邻用户数
        int[] ccc=new int[ttt.length];//总权值
        int[] lll=new int[ttt.length];//相邻相邻用户数
        for(int i=0;i<nnn.length;i++){
        	for(int j=0;j<node_relation[i][max_num_of_edge-1];j++){//邻边
        		for(int q=0;q<node_relation[j][max_num_of_edge-1];q++){//邻边的邻边
        			for(int k=0;k<uuu.length;k++){
            			if(uuu[k][0]==node_relation[j][q]){
            				lll[i]++;break;
            			}
            		}
        		}
        		nnn[i]+=cap[i][node_relation[i][j]];
        		ccc[i]+=cost[i][node_relation[i][j]];
        		for(int k=0;k<uuu.length;k++){
        			if(uuu[k][0]==node_relation[i][j]){
        				mmm[i]++;break;
        			}
        		}
        	}
        }
        
        
        //System.out.println("序号\t临边数\t总流量\t临近用户数\t是否直连\t非直连成本\t服务器\t总权值\t二级相邻用户");
        for(int i=0;i<ttt.length;i++){
        	int flag=0;
        	//System.out.print(i+"\t");
        	rrr[i][0]=i;
        	//System.out.print(node_relation[i][max_num_of_edge-1]+"\t");
        	rrr[i][1]=node_relation[i][max_num_of_edge-1];
        	//System.out.print(nnn[i]+"\t");
        	rrr[i][2]=nnn[i];
        	//System.out.print(mmm[i]+"\t");
        	rrr[i][3]=mmm[i];
        	for(int j=0;j<uuu.length;j++){
        		if(uuu[j][0]==i){
        			//System.out.print("1\t");
        			rrr[i][4]=1;
        			//System.out.print(min_score[j]+"\t");
        			rrr[i][5]=min_score[j];
        			flag=1;
        			break;
        		}
        	}
        	if(flag==0)
        	{
        		//System.out.print("0\t");
        		rrr[i][4]=0;
        		//System.out.print("0\t");
        		rrr[i][5]=0;
        	}
        	//System.out.print(ttt[i]+"\t");
        	rrr[i][6]=ttt[i];
        	rrr[i][7]=ccc[i];
        	rrr[i][8]=lll[i];
        	//System.out.println();
        }
        
    	//System.out.println(FindBestScore(cap,iii));
    	return rrr;
	}
	private static void PreOperation() {//准备工作，求得每个直连节点不部署服务器的最低成本

        min_score = new int[user_info.length];
        int loc = 0;
        for (int i = 0; i < user_info.length; i++) {
            int[] ne_cap = new int[20];
            int[] ne_cost = new int[20];
            int ne_count = 0;
            int nodeid = user_info[i][0];
            int node_num = node_relation[nodeid][max_num_of_edge - 1];
            for (int j = 0; j < node_num; j++) {
                int tmp_cap = cap[nodeid][node_relation[nodeid][j]];
                int tmp_cost = cost[nodeid][node_relation[nodeid][j]];
                // System.out.print(tmp_cap+"&"+tmp_cost+" ");
                if (ne_count == 0) {
                    ne_cost[ne_count] = tmp_cost;
                    ne_cap[ne_count] = tmp_cap;
                    ne_count++;
                    continue;
                }
                for (int k = ne_count; k >= 0; k--) {
                    if (k == 0) {
                        ne_cost[k] = tmp_cost;
                        ne_cap[k] = tmp_cap;
                        ne_count++;
                        break;
                    }
                    if (ne_cost[k - 1] < tmp_cost || (ne_cost[k - 1] == tmp_cost && ne_cap[k - 1] < tmp_cap)) {
                        ne_cost[k] = tmp_cost;
                        ne_cap[k] = tmp_cap;
                        ne_count++;
                        break;
                    } else {
                        ne_cost[k] = ne_cost[k - 1];
                        ne_cap[k] = ne_cap[k - 1];
                        ne_cost[k - 1] = tmp_cost;
                        ne_cap[k - 1] = tmp_cap;
                    }
                }

            }
            int tmp_sp = 0;
            int tmp_need = user_info[i][1];

            for (int l = 0; l < ne_count; l++) {
                if (ne_cap[l] >= tmp_need) {
                    tmp_sp += tmp_need * ne_cost[l];
                    // System.out.println(tmp_need+"*"+ne_cost[l]+"="+tmp_sp);
                    tmp_need -= tmp_need;
                    break;
                } else {
                    tmp_sp += ne_cap[l] * ne_cost[l];
                    // System.out.print(ne_cap[l]+"*"+ne_cost[l]+"+");
                    tmp_need -= ne_cap[l];
                }
            }
            min_score[loc++] = tmp_sp;
            // System.out.println();
            // System.out.println("*******************************************");
        }
        // for(int
        // i=0;i<min_score.length;i++)System.out.print(i+":"+user_info[i][0]+"\t");
        // System.out.println();
        // for(int i:min_score)System.out.print(i+"\t");
        // System.out.println();
    }

    public static String[] GeneticAlgorithm() {// 遗传算法

        
    	int[] vst=new int[user_info.length];
        int min = INT_MAX;
        Individuals in = new Individuals(cap.length, user_info, 999);
        in.ConstantBorn(min_score,SPEND,B);// 固定出生
        int BestNum = POP_SIZE * CHOOSE_RATE / 100;
        Individuals[] Best_Ind = new Individuals[BestNum];// 优胜个体
        int in_score = FindBestScore(cap, in);
        System.out.println("Init start:" + in_score);


        
        int[][] info=ppp(in);
    	
//        for(int i=0;i<info.length;i++){
//        	for(int j=0;j<info[i].length;j++){
//        		System.out.print(info[i][j]+"\t");
//        	}
//        	System.out.println();
//        }
        
        if (POP_SIZE>1&&POP_SIZE <22) {
            // 优化初始化

            Individuals test = new Individuals(cap.length, user_info, 0);
            test.Copy(in);
            int[] tmp_gene = in.getGene();
            int[] init_gene = in.getGene();
            for (int i = 0; i < tmp_gene.length; i++) {
            //for (int i = tmp_gene.length-1; i >=0; i--) {
                if (tmp_gene[i] == 1) {
                    tmp_gene[i] = 0;
                    test.restore();
                    test.setGene(tmp_gene);

                    int tmp_score = FindBestScore(cap, test);

                    if (tmp_score < in_score) {
                    	
                    	
                    	//System.out.print(i+"\t");
                    	int j;
                    	for(j=0;j<user_info.length;j++){
                    		if(user_info[j][0]==i)break;
                    	}
                    	//System.out.println(min_score[j]);
                    	vst[j]=1;
                    	
                    	
                        in_score = tmp_score;
                        init_gene[i] = 0;
                    } else {
                        test.restore();
                        test.setGene(init_gene);
                        tmp_gene[i] = 1;
                    }
                }
            }
            in.Copy(test);
            System.out.println("Best start:" + in_score);
//            System.out.println(in.CountGene());
//            for(int k=0;k<user_info.length;k++){
//            	System.out.println(min_score[k]+"\t"+vst[k]);
//            }
        }

        /* 选出优胜个体最为最后结果 */
        Individuals Ind = new Individuals(cap.length, user_info, 999);
        Ind.Copy(in);
        int[] Best_Score = new int[BestNum];// 记录最优个体的分数
        for (int i = 0; i < BestNum; i++) {

        	if(POP_SIZE>1&&POP_SIZE<22){
                Best_Ind[i]=new Individuals(cap.length,user_info,i);
                Best_Ind[i].Copy(in);
                Best_Ind[i].setId(i);
                Best_Score[i] = in_score;
            }
            else{
                Best_Ind[i] = new Individuals(cap.length, user_info, i);
                Best_Ind[i].RandomBorn(node_relation,info, SPEND, A, B);
                Best_Score[i] = FindBestScore(cap, Best_Ind[i]);
            }
        }

        /* 组成初始种群 */
        Individuals[] pops = new Individuals[POP_SIZE];
        for (int i = 0; i < POP_SIZE; i++) {// 初始种群完全由个体多次变异而得
            pops[i] = new Individuals(cap.length, user_info, i);
            pops[i].RandomBorn(node_relation,info, SPEND, A, B);
            pops[i].setRate(VARITION_RATE);// 正常变异概率
        }

        /*
         * 交叉，变异，适应度计算
         */
        int[][] caps = new int[cap.length][cap.length];
        RestoreCap(node_relation, caps);

        int[] score = new int[POP_SIZE];
        for (int j = 0; j < score.length; j++)
            score[j] = INT_MAX;

        outer: for (int i = 0; i < Gap; i++) {
            if (i % D + C == 0)
                System.out.print("line:" + i);

            long RUN_TIME=System.currentTimeMillis()- START_TIME;
            // 超时记录
            if ( RUN_TIME > MAX_TIME) {
                System.out.println("Over time,iterator " + i + " times");
                break;// 超时退出
            }
            
            // 动态交叉率
            MAT_RATE = MAX_MAT_RATE - i * dec_rate < MIN_MAT_RATE ? MIN_MAT_RATE : (int) (MAX_MAT_RATE - i * dec_rate);
            // 动态变异率
            VARITION_RATE = MIN_VA_RATE + i * inc_rate > MAX_VA_RATE ? MAX_VA_RATE : (int) (MIN_VA_RATE + i * inc_rate);

            if (i % D + C == 0)
                System.out.println(" MAT-"+MAT_RATE+" VA-"+VARITION_RATE+" RUN "+RUN_TIME);

            for (int j = 0; j < POP_SIZE; j++) {// 计算适应度
                if (System.currentTimeMillis() - START_TIME > MAX_TIME) {
                    System.out.println("Over time,iterator " + i + " times");
                    break outer;// 超时退出
                }
                score[j] = FindBestScore(caps, pops[j]);
                if (i % D + C == 0)
                    System.out.print(score[j] + " ");
            }
            if (i % D + C == 0)
                System.out.println();

            ChooseIndividuals(pops, score, Best_Ind, Best_Score);// 保存优胜个体,淘汰低质个体，用优胜个体大概率变异产生新个体加入种族

            mat(pops);// 交叉

            for (int j = 0; j < POP_SIZE; j++) {
                pops[j].setRate(VARITION_RATE);
                pops[j].Varition();// 变异
            }
        }
        // 获取最优个体
        int id = getMinId(Best_Score);
        Ind.Copy(Best_Ind[id]);
        String[] res = setRoad(caps, Ind);
        min = Best_Score[id];

        // 错误检查
        System.out.println("Genetic check:");
        checkAns(cap, user_info, res);
        System.out.println("Init best:" + in_score);
        System.out.println("Genetic best:" + min);
        System.out.println("Run find road " + RUNTIMES + " times");
        Ind.Print("gene");

        return res;
    }

    public static void mat(Individuals[] Ind) {// 交叉操作
        // System.out.println("**************************************************");
        // 确定交叉方式
        int point, tmp, i, j, loc, ch;
        int[] index = new int[POP_SIZE];
        for (i = 0; i < POP_SIZE; i++) {
            index[i] = i;
        }

        for (i = 0; i < POP_SIZE; i++) {
            point = r.nextInt(POP_SIZE - i);
            tmp = index[i];
            index[i] = index[point + i];
            index[point + i] = tmp;
        }

        // 正式交叉
        for (i = 0; i < POP_SIZE; i += 2) {
            if (r.nextInt(101) > MAT_RATE)
                continue;
            loc = r.nextInt(cap.length);// 交换位置
            int[] gene1 = Ind[index[i]].getGene();
            int[] gene2 = Ind[index[i + 1]].getGene();
            // printArr(gene1);
            // printArr(gene2);
            for (j = loc; j < gene1.length; j++) {
                // 段交叉
                ch = gene1[j];
                gene1[j] = gene2[j];
                gene2[j] = ch;
            }
            Ind[index[i]].setGene(gene1);
            Ind[index[i + 1]].setGene(gene2);
            // printArr(gene1);
            // printArr(gene2);
            // System.out.println("**************************************************");
        }
    }

    public static void ChooseIndividuals(Individuals[] Ind, int[] score, Individuals[] Best_Ind, int[] Best_Score) {// 适应度计算
        Map<Integer, Integer> m = new HashMap<Integer, Integer>();// 个体id、成本

        // 定义
        int i;
        for (i = 0; i < score.length; i++) {
            m.put(i, score[i]);
        }

        List<Entry<Integer, Integer>> list = new ArrayList<Entry<Integer, Integer>>(m.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return (o1.getValue() - o2.getValue());
            }
        });

        // 选择前几个优秀个体尝试替换优质种群中的个体
        int choose_num = POP_SIZE * CHOOSE_RATE / 100;
        int choose_minscore_id;
        int best_maxscore_id;
        int choose_maxscore_id;
        for (i = 0; i < choose_num; i++) {
            choose_minscore_id = list.get(i).getKey();
            best_maxscore_id = getMaxId(Best_Score);
            if (Best_Score[best_maxscore_id] > score[choose_minscore_id]) {// 发现更优，替换优质种群
                Best_Score[best_maxscore_id] = score[choose_minscore_id];
                Best_Ind[best_maxscore_id].Copy(Ind[choose_minscore_id]);
            }
        }

        // 将非优质的随机个体用优质个体替换
        for (i = 0; i < choose_num; i++) {
            int loc = r.nextInt(POP_SIZE - choose_num);
            choose_maxscore_id = list.get(loc).getKey();
            Ind[choose_maxscore_id].Copy(Best_Ind[i]);
            // Ind[choose_maxscore_id].RandomBorn(min_score, A, B);
            Ind[choose_maxscore_id].setRate(VARITION_RATE);
        }
    }

    public static String[] setRoad(int[][] caps, Individuals Ind) {// 获得结果路径
        String[] res = new String[max_num_of_road];// 保存结果
        int count = 0;
        int flag = 0;
        int[][] user_infos = Ind.getUser_info();

        getRoad(Ind);
        for (int i = 0; i < user_infos.length; i++) {// 预先分配流量
            if (user_infos[i][1] == 0) {
                String str = user_info[i][0] + " " + i + " " + user_info[i][1];
                res[count++] = str;
            }
        }
        int num_of_road = min_cost_max_flux[max_num_of_road - 1][max_num_of_node - 1];// 总路数
        int now_loc = 0;// 记录当前第几条路
        while (flag != 1) {

            flag = 1;
            for (int n = 0; n < user_infos.length; n++) {
                if (user_infos[n][1] != 0) {
                    flag = 0;
                }
            }

            if (flag != 1) {// 等于1表示全部需求为0，不用判断

                if (now_loc == num_of_road) {// 所有路都遍历完了
                    for (int n = 0; n < user_infos.length; n++) {
                        if (user_infos[n][1] != 0) {
                            String str = user_infos[n][0] + " " + n + " " + user_infos[n][1];
                            res[count++] = str;
                            System.out.println("ERROR:" + str);
                        }
                    }
                    break;

                }
                int arr_size = max_num_of_node;
                for (int i = now_loc; i < num_of_road;) {
                    String tmp = "";
                    // 0 1 2 3 4 ... len-3 len-2 len-1
                    // cap cost start mid1 mid2... pre end userid
                    int len = min_cost_max_flux[i][arr_size - 1];
                    int userid = min_cost_max_flux[i][len - 1];
                    int use = min_cost_max_flux[i][0];
                    int need = user_infos[userid][1];
                    if (need == 0) {// 需求为0,直接删除此条路径(无需供给)
                        now_loc++;
                        i++;
                        continue;
                    }
                    if (need < use) {// 供给大于需求
                        use = need;
                    }
                    user_infos[userid][1] -= use;
                    int k;
                    for (k = 2; k < len - 2; k++) {
                        int start = min_cost_max_flux[i][k];
                        int end = min_cost_max_flux[i][k + 1];
                        caps[start][end] -= use;
                        tmp += start + " ";
                    }
                    tmp += min_cost_max_flux[i][k] + " " + userid + " " + use;
                    res[count++] = tmp;
                    now_loc++;
                    i++;
                }
            }
        }
        RestoreCap(node_relation, caps);
        String[] back = new String[count];
        for (int i = 0; i < count; i++)
            back[i] = res[i];
        return back;
    }

    public static int FindBestScore(int[][] caps, Individuals Ind) {// 寻找最优路径
        RUNTIMES++;
        int spend = 0;// 选路成本
        int flag = 0;// 判断是否可以结束寻路
        int[][] user_infos = Ind.getUser_info();

        getRoad(Ind);
        spend += Ind.CountGene() * SPEND;// 计算服务器成本
        int num_of_road = min_cost_max_flux[max_num_of_road - 1][max_num_of_node - 1];// 总路数
        int now_loc = 0;// 记录当前第几条路
        while (flag != 1) {

            flag = 1;
            for (int n = 0; n < user_infos.length; n++) {
                if (user_infos[n][1] != 0) {
                    flag = 0;
                }
            }
            if (flag != 1) {// 等于1表示全部需求为0，不用判断
                if (now_loc == num_of_road) {
                    // spend = INT_MAX;
                    // 无解的惩罚函数
                    for (int n = 0; n < user_infos.length; n++) {
                        if (user_infos[n][1] != 0) {
                        	//System.out.println(user_infos[n][0]+"->"+n+user_info[n][1]);
                            spend += SPEND;
                        }
                    }
                    RestoreCap(node_relation, caps);
                    return spend;
                }
                int arr_size = max_num_of_node;
                for (int i = now_loc; i < num_of_road;) {
                    // 0 1 2 3 4 ... len-3 len-2 len-1
                    // cap cost start mid1 mid2... pre end userid
                    int len = min_cost_max_flux[i][arr_size - 1];
                    int userid = min_cost_max_flux[i][len - 1];
                    int use = min_cost_max_flux[i][0];
                    int need = user_infos[userid][1];
                    int costs = min_cost_max_flux[i][1];
                    if (need == 0) {// 需求为0,直接删除此条路径(无需供给)
                        if (now_loc == num_of_road) {
                            for (int n = 0; n < user_infos.length; n++) {
                                if (user_infos[n][1] != 0) {
                                    spend += SPEND;
                                    //System.out.println("NOT ENOUGH:"+user_infos[n][0]+"->"+n+" "+user_info[n][1]);
                                }
                            }
                            RestoreCap(node_relation, caps);
                            return spend;
                        }
                        i++;
                        now_loc++;
                        continue;
                    }
                    if (need < use) {// 供给大于需求
                        use = need;
                    }
                    user_infos[userid][1] -= use;
                    int k;
                    for (k = 2; k < len - 2; k++) {
                        int start = min_cost_max_flux[i][k];
                        int end = min_cost_max_flux[i][k + 1];
                        caps[start][end] -= use;
                         //System.out.print(start +
                         //"-("+(caps[start][end]+use)+")->");
                    }
                    spend += use * costs;
                     //System.out.println(min_cost_max_flux[i][k]+" "+use+" user need "+
                     //(user_infos[userid][1] + use)+" spend "+
                     //(spend-Ind.CountGene() * SPEND));
                    i++;
                    now_loc++;
                }
            }
        }
        RestoreCap(node_relation, caps);
        return spend;
    }
    
    public static boolean spfa(int[][] caps, Individuals Ind, int[] end, int[] pre) {
        int len = cap.length;// 得到总节点数

        int[] dis = new int[len];// 记录起点到各个点的价格
        int[] vst = new int[len];// 判断当前节点是否访问过
        int[][] user_infos = Ind.getUser_info();// 当前个体用户的需求量
        int[] gene = Ind.getGene();// 服务器分布情况
        end[0] = -1;// end[0]保存最后一个

        int[] Q = new int[len + 10];// 队列
        int head = 0;// 队列头
        int tail = 0;// 队列尾
        for (int i = 0; i < len; i++) {// 选出超级源点的直连点(服务器位置)
            if (gene[i] == 1) {
                dis[i] = 0;
                pre[i] = -1;
                Q[tail++] = i;
                tail = tail == len + 10 ? 0 : tail;
            } else {
                dis[i] = INT_MAX;
            }
        }
        int loc;
        while (head != tail) {// 队列空时退出
            loc = Q[head++];
            head = head == len + 10 ? 0 : head;
            vst[loc] = 1;
            int tmp_node_num = node_relation[loc][max_num_of_edge - 1];
            for (int k = 0; k < tmp_node_num; k++) {// 取所有临边
                int node = node_relation[loc][k];
                if (caps[loc][node] != 0 && cost[loc][node] + dis[loc] < dis[node]) {
                    dis[node] = cost[loc][node] + dis[loc];
                    pre[node] = loc;
                    if (vst[node] != 1) {
                        // SLF优化
                        if (dis[node] < dis[loc]) {
                            head = head == 0 ? len + 9 : head - 1;
                            Q[head] = node;
                            vst[node] = 1;
                        }

                        else {
                            Q[tail++] = node;
                            tail = tail == len + 10 ? 0 : tail;
                            vst[node] = 1;
                        }
                    }
                }
            }
            vst[loc] = 0;
        }
        // 判断用户需求是否都满足
        int tmp_min = INT_MAX;
        int tmp_loc = -1;
        for (int j = 0; j < user_infos.length; j++) {
            if (user_infos[j][1] != 0) {
                int webnodeid = user_infos[j][0];
                if (dis[webnodeid] == INT_MAX)
                    continue;
                if (tmp_min > dis[webnodeid]) {
                    tmp_min = dis[webnodeid];
                    tmp_loc = webnodeid;
                }
            }
        }
        end[0] = tmp_loc;
        if (tmp_loc != -1)
            return true;
        return false;

    }

    public static void ek(int[][] caps, Individuals Ind, int[] end, int[] pre, int[][] l) {// s->t

        int num_of_road = l[max_num_of_road - 1][max_num_of_node - 1];
        int[][] user_infos = Ind.getUser_info();

        int t = end[0];// t为终点
        int count = 3;// 从第三位开始记录路径，前两位为 总带宽供应、总花费、用户序号(不是用户直连点的序号)
        int i, sum = INT_MAX;// sum为当前路径中允许通过的最大流量
        int costs = 0;// 记录路径中花费之和
        for (i = t; pre[i] != -1; i = pre[i]) {
            sum = Math.min(sum, caps[pre[i]][i]);
            l[num_of_road][count++] = i;
        }
        l[num_of_road][count++] = i;
        l[num_of_road][max_num_of_node - 1] = count;// 标记数组长度

        int id = l[num_of_road][3];// 第一个节点直连用户
        for (int p = 0; p < user_info.length; p++) {
            if (user_info[p][0] == id) {
                id = p;
                break;
            }
        }
        // 新取得的id为用户的序号
        l[num_of_road][2] = id;

        // 直接为用户分配流量
        int use = user_infos[id][1];
        if (sum > use)
            sum = use;
        user_infos[id][1] -= sum;
        // 计算每条路应该减少的带宽

        for (i = t; pre[i] != -1; i = pre[i]) {
            caps[pre[i]][i] -= sum;
            costs += cost[pre[i]][i];
        }

        reverseArr(l[num_of_road], 2, count - 1);// 存储的路径是反向的，需倒置
        // 之后要反复读取带宽最大量，放0号位可能快些
        l[num_of_road][1] = costs;
        l[num_of_road][0] = sum;
        l[max_num_of_road - 1][max_num_of_node - 1]++;// 增加了一条路径，应当记住
        Ind.setUser_info(user_infos);// 不断更改用户需求
    }

    public static void getRoad(Individuals Ind) {// 最小费用最大流
        // long ttt=System.currentTimeMillis();
        // System.out.print("start:");

        int[][] caps = new int[cap.length][cap.length];
        RestoreCap(node_relation, caps);// 首先将流量图恢复
        int[] pre = new int[cap.length];// 保存每个点的前节点
        min_cost_max_flux[max_num_of_road - 1][max_num_of_node - 1] = 0;
        int[] info = new int[1];
        while (spfa(caps, Ind, info, pre))// 反复获取最短路径，并减去路上带宽
            ek(caps, Ind, info, pre, min_cost_max_flux);// 获取所有结果路径，存在数组中
        Ind.restore();// Ind的用户表改变了，必须先恢复所有需求
        Ind.update();// Ind的用户表恢复后，重新更新用户所需资源(直连服务器无需资源)

        // System.out.println(System.currentTimeMillis() - ttt);
    }

    // 功能函数，包括打印、复制、取最值、反转、检查、初始化
    public static void printArr(int[][] arr) {
        int num_of_road = arr[max_num_of_road - 1][max_num_of_node - 1];
        for (int i = 0; i < num_of_road; i++) {
            int num_of_node = arr[i][max_num_of_node - 1];
            for (int j = 0; j < num_of_node; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void printArr(int[] arr) {// 打印数组
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
    public static int getMinId(int[] arr) {// 取数组最大
        int min = INT_MAX;
        int loc = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
                loc = i;
            }
        }
        return loc;
    }
    public static int getMaxId(int[] arr) {// 取数组最大
        int max = 0;
        int loc = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                loc = i;
            }
        }
        return loc;
    }
    private static String[] DealWithRes(String[] firstResult) {// 将输出标准化
        int len = firstResult.length;
        String[] res = new String[len + 2];
        res[0] = String.valueOf(len);
        res[1] = "";
        for (int i = 0; i < len; i++) {
            res[i + 2] = firstResult[i];
        }
        return res;
    }
    public static String[] FirstResult() {// 直接将服务器部署在消费节点上测试
        int ConsumeNum = user_info.length;
        String[] res = new String[ConsumeNum];
        for (int i = 0; i < ConsumeNum; i++) {
            res[i] = user_info[i][0] + " " + i + " " + user_info[i][1];
        }
        return res;
    }
    public static void CopyArr(int[][] tmp,int[][] arr){
    	for(int i=0;i<arr.length;i++){
    		for(int j=0;j<arr[i].length;j++){
    			tmp[i][j]=arr[i][j];
    		}
    	}
    }
    public static void CopyArr(int[] tmp,int[] arr){
    	for(int i=0;i<arr.length;i++){
    		tmp[i]=arr[i];
    	}
    } 
    public static void RestoreCap(int[][] tmp_node_relation, int[][] tmp_cap) {// 流量图恢复原始
        for (int i = 0; i < cap.length; i++) {
            int neighbor_node_num = tmp_node_relation[i][max_num_of_edge - 1];
            for (int j = 0; j < neighbor_node_num; j++) {
                int start = i;
                int end = tmp_node_relation[i][j];
                tmp_cap[start][end] = hid_cap[start][end];
                tmp_cap[end][start] = hid_cap[end][start];
            }
        }
    }
    public static void reverseArr(int[] arr, int s, int t) {// 数组指定位置反转
        while (s < t) {// 将数组从s开始t结束的部分倒置,范围为[s,t]
            int tmp = arr[s];
            arr[s] = arr[t];
            arr[t] = tmp;
            s++;
            t--;
        }
    }
    public static void printCap(int[][] caps, int flag) {// 输出全流量或输出某一节点的全流量
        if (flag == -1) {// 打印全图
            for (int i = 0; i < caps.length; i++) {
                System.out.print("节点:" + i + "~");
                for (int j = 0; j < caps.length; j++) {
                    if (caps[i][j] != 0)
                        System.out.print(j + ":" + caps[i][j] + " ");
                }
                System.out.println();
            }
        } else {// 打印一个点的所有临边
            System.out.print("节点:" + flag + "~");
            for (int j = 0; j < caps.length; j++) {
                if (caps[flag][j] != 0)
                    System.out.print(j + ":" + caps[flag][j] + " ");
            }
            System.out.println();
        }
    }
    public static boolean checkAns(int[][] caps, int[][] user_infos, String[] s) {
        // 检查输出结果是否正确
        for (int i = 0; i < s.length; i++) {
            String[] tmp = s[i].split("\\s+");// tmp保存的是一条路径
            int pro = Integer.parseInt(tmp[tmp.length - 1]);
            // 0 1 2 3 ... len-3 len-2 len-1
            // start mid1 mid2 mid3 ... end userid need
            for (int j = 0; j < tmp.length - 2; j++) {
                int start = Integer.parseInt(tmp[j]);
                int end = Integer.parseInt(tmp[j + 1]);
                if (j == tmp.length - 3)
                    end = user_infos[end][0];
                if (start == end)// 已经到达用户了
                    continue;

                caps[start][end] -= pro;
                if (caps[start][end] < 0) {// 发现有路径超过最大流量，返回错误
                    System.out
                            .println("CAP NOT ENOUGH:" + start + "->" + end + ":" + caps[start][end] + pro + "<" + pro);
                    return false;
                }
            }
            // 一条路径分配完毕，减去用户的需求量
            user_infos[Integer.parseInt(tmp[tmp.length - 2])][1] -= pro;
        }
        for (int i = 0; i < user_infos.length; i++) {
            if (user_infos[i][1] > 0) {// 需求没有完全满足
                System.out.println("NEED NOT ENOUGH: USER " + i + " NEED " + user_infos[i][1]);
                return false;
            }
        }
        // 检查无误
        System.out.println("CHECK OK");
        return true;
    }
    public static boolean checkCap(int[][] caps) {// 判断流量地图是否被修改
        for (int i = 0; i < caps.length; i++) {
            for (int j = 0; j < caps.length; j++) {
                if (caps[i][j] != hid_cap[i][j])
                    return false;
            }
        }
        return true;
    }
    private static void AddRelation(int[][] tmp,int s,int t){
    	//System.out.println(s+" "+t+" "+tmp.length+" "+tmp[0].length);
    	tmp[s][tmp[s][max_num_of_edge - 1]++] = t;
        tmp[t][tmp[t][max_num_of_edge - 1]++] = s;
    }
    private static void InitMap(String[] graphContent) {// 初始化
        // 网络节点数量 网络链路数量 消费节点数量
        String[] NodeInfo = graphContent[0].split("\\s+");
        int NodeNum = Integer.parseInt(NodeInfo[0]);
        int RoadNum = Integer.parseInt(NodeInfo[1]);
        int ConsumeNum = Integer.parseInt(NodeInfo[2]);

        // 为成员初始化空间
        cap = new int[NodeNum][NodeNum];
        cost = new int[NodeNum][NodeNum];
        user_info = new int[ConsumeNum][2];
        hid_cap = new int[NodeNum][NodeNum];
        min_cost_max_flux = new int[max_num_of_road][max_num_of_node];

        // 2.改进数据结构
        node_relation = new int[NodeNum][max_num_of_edge];
//        tmp_node_relation = new int[NodeNum+2][max_num_of_edge];
//     
//        MAXN=cap.length+10;
//        inf=INT_MAX;  
//        pre=new int[MAXN];          
//        dis=new int[MAXN];          
//        vis=new boolean[MAXN];   
//        path=new int[MAXN];  
//        head=new int[MAXN];  
//        Edge=new int[MAXN*30][5];
//        tmp_Edge=new int[MAXN*30][5];
//        tmp_cap=new int[MAXN][MAXN];
//        tmp_cost=new int[MAXN][MAXN];
//        tmp_head=new int[MAXN];
//        tmp_node=new int[MAXN];
//        tmp_visit=new int[MAXN];
//        stack=new int[MAXN];
        cap_info=new int[RoadNum][4];
        
//        for(int i=0;i<MAXN;i++)head[i]=-1;

        // 默认地图所有路径不通
        for (int i = 0; i < NodeNum; i++) {
            for (int j = 0; j < NodeNum; j++) {
                cap[i][j] = FLUX_MIN;
                cost[i][j] = INT_MAX;
            }
        }

        SPEND = Integer.parseInt(graphContent[2]);// 服务器价钱

        int Line = 4;// 第4行数据
        for (Line = 4; Line < 4 + RoadNum; Line++) {
            String[] TmpArr = graphContent[Line].split("\\s+");
            // 起点 终点 带宽 单价
            int Start = Integer.parseInt(TmpArr[0]);
            int End = Integer.parseInt(TmpArr[1]);
            int Flux = Integer.parseInt(TmpArr[2]);
            int Cost = Integer.parseInt(TmpArr[3]);

            cap[Start][End] = cap[End][Start] = Flux;
            cost[Start][End] = cost[End][Start] = Cost;
            
//            addEdge(Edge,head,Start,End,Flux,Cost);
//            addEdge(Edge,head,End,Start,Flux,Cost);
            
            cap_info[Line-4][0]=Start;
            cap_info[Line-4][1]=End;
            cap_info[Line-4][2]=Flux;
            cap_info[Line-4][3]=Cost;
        }

        // 设置隐藏流量图以防止流量图被意外修改未归位
        for (int i = 0; i < NodeNum; i++) {
            for (int j = 0; j < NodeNum; j++) {
                hid_cap[i][j] = cap[i][j];
            }
        }

        // 2.改进数据结构
        for (Line = 4; Line < 4 + RoadNum; Line++) {
            String[] TmpArr = graphContent[Line].split("\\s+");
            // 起点 终点 带宽 单价
            int Start = Integer.parseInt(TmpArr[0]);
            int End = Integer.parseInt(TmpArr[1]);
            // node_relation[i][len-1]=节点i的临边数
            AddRelation(node_relation,Start,End);
        }

        // 消费结点
        for (int i = Line + 1; i < Line + 1 + ConsumeNum; i++) {
            // 相邻结点 消耗
            String[] TmpArr = graphContent[i].split("\\s+");
            int Id = Integer.parseInt(TmpArr[0]);
            int Neighbor = Integer.parseInt(TmpArr[1]);
            int Needs = Integer.parseInt(TmpArr[2]);

            // 记录用户信息
            user_info[Id][0] = Neighbor;
            user_info[Id][1] = Needs;
        }
    }

}
