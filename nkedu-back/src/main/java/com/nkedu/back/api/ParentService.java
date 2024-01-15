package com.nkedu.back.api;

import java.util.List;

import com.nkedu.back.model.Parent;
import com.nkedu.back.model.ParentDto;

public interface ParentService {

	/**
	 * �θ�� ���� ���� (��ū�� �ʿ���)
	 * @param parent
	 * @return Parent
	 */
	public boolean createParent(ParentDto parentDto);

	/**
	 * �θ�� ���� ���� (��ū�� �ʿ���)
	 * @param id
	 */
	public boolean deleteParentById(Long id);
	
	/**
	 * �θ�� ���� ���� (��ū�� �ʿ���)
	 * @param parent
	 * @return
	 */
	public boolean updateParent(Long id, ParentDto parentDto);

	/**
	 * �θ�� ���� ����Ʈ ��ȸ
	 * @return List<Parent>
	 */
	public List<ParentDto> getParents();

	/**
	 * �θ�� ���� ���� ��ȸ
	 * @param id
	 * @return Parent
	 */
	public ParentDto getParentById(Long id);

	
}
