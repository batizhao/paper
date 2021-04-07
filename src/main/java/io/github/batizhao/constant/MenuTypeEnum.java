/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.github.batizhao.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lengleng
 * @date 2020-02-17
 * <p>
 * 菜单类型
 */
@Getter
@RequiredArgsConstructor
public enum MenuTypeEnum {

	/**
	 * 左侧菜单
	 */
	MENU("M", "menu"),

	/**
	 * 顶部菜单
	 */
//	TOP_MENU("T", "top"),

	/**
	 * 按钮
	 */
	BUTTON("B", "button");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
