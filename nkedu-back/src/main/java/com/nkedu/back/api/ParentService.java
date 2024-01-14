package com.nkedu.back.api;

import java.util.List;

import com.nkedu.back.model.Parent;

public interface ParentService {

	/**
	 * �θ�� ���� ���� (��ū�� �ʿ���)
	 * @param parent
	 * @return Parent
	 */
	public Parent createParent(Parent parent);

	/**
	 * �θ�� ���� ���� (��ū�� �ʿ���)
	 * @param id
	 */
	public void deleteParent(Long id);
	
	/**
	 * �θ�� ���� ���� (��ū�� �ʿ���)
	 * @param parent
	 * @return
	 */
	public Parent updateParent(Parent parent);

	/**
	 * �θ�� ���� ����Ʈ ��ȸ
	 * @return List<Parent>
	 */
	public List<Parent> getParents();

	/**
	 * �θ�� ���� ���� ��ȸ
	 * @param id
	 * @return Parent
	 */
	public Parent getParent(Long id);

	
}
