package s3.base;

import java.util.LinkedList;
import java.util.List;

public class S3Action {
	public static final int ACTION_NONE = -1;
	public static final int ACTION_MOVE = 0;
	public static final int ACTION_ATTACK = 1;
	public static final int ACTION_HARVEST = 2;
	public static final int ACTION_STAND_GROUND = 3;
	public static final int ACTION_REPAIR = 4;
	public static final int ACTION_TRAIN = 5;
	public static final int ACTION_BUILD = 6;
	public static final int ACTION_CANCEL = 7;
	
	public static final char[] quick_keys = {'M','A','H','S','R','T','B','C'};
        public static final String[] action_names ={"Move", "Attack", "Harvest", "Stand_Ground", "Repair", "Train", "Build", "Cancel"};

	public int m_targetUnit;
	public int m_action = ACTION_NONE;
	public List<Object> m_parameters = new LinkedList<Object>();
	
	public S3Action(S3Action a) {
		m_targetUnit = a.m_targetUnit;
		m_action = a.m_action;
		m_parameters.addAll(a.m_parameters);
	}

	public S3Action(int targetUnit, int a) {
		m_targetUnit = targetUnit;
		m_action = a;
	}

	public S3Action(int targetUnit, int a,Object p1) {
		m_targetUnit = targetUnit;
		m_action = a;
		m_parameters.add(p1);
	}
	public S3Action(int targetUnit, int a,Object p1,Object p2) {
		m_targetUnit = targetUnit;
		m_action = a;
		m_parameters.add(p1);
		m_parameters.add(p2);
	}
	public S3Action(int targetUnit, int a,Object p1,Object p2,Object p3) {
		m_targetUnit = targetUnit;
		m_action = a;
		m_parameters.add(p1);
		m_parameters.add(p2);
		m_parameters.add(p3);
	}
	public S3Action(int targetUnit, int a,Object p1,Object p2,Object p3,Object p4) {
		m_targetUnit = targetUnit;
		m_action = a;
		m_parameters.add(p1);
		m_parameters.add(p2);
		m_parameters.add(p3);
		m_parameters.add(p4);
	}

    public Integer getIntParameter(int p) {
        return (Integer)(m_parameters.get(p));
    }

    public boolean equals(Object o) {
        if (o instanceof S3Action) {
            S3Action a = (S3Action)o;

            if (m_targetUnit!=a.m_targetUnit) return false;
            if (m_action!=a.m_action) return false;
            if (m_parameters.size()!=a.m_parameters.size()) return false;
            for(int i = 0;i<m_parameters.size();i++) {
                Object p1 = m_parameters.get(i);
                Object p2 = a.m_parameters.get(i);
                if (!p1.equals(p2)) return false;
            }
            return true;
        }
        return false;
    }
    
    public String toString() {
        return "Action(" + m_targetUnit + "," + action_names[m_action] + ", " + m_parameters + ")";
    }
}
