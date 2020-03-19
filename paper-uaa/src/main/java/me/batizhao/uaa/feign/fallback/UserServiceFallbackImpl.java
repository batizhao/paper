/*
 *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.batizhao.uaa.feign.fallback;

import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.core.vo.RoleVO;
import me.batizhao.ims.core.vo.UserVO;
import me.batizhao.uaa.feign.UserFeignService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lengleng
 * @date 2019/2/1
 */
@Slf4j
@Component
public class UserServiceFallbackImpl implements UserFeignService {
    @Setter
    private Throwable cause;

    @Override
    public ResponseInfo<UserVO> getByUsername(String username, String from) {
        log.error("feign 查询用户信息失败: {}", username, cause);
        return null;
    }

    @Override
    public ResponseInfo<List<RoleVO>> getRolesByUserId(Long userId, String from) {
        log.error("feign 查询用户角色信息失败: {}", userId, cause);
        return null;
    }
}
