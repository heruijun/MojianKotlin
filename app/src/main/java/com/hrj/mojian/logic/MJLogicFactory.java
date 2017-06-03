package com.hrj.mojian.logic;

public class MJLogicFactory {

	private static MJLogic mjLogic = new MJLogicImpl();

	public static MJLogic getMJLogic() {
		return mjLogic;
	}

}
