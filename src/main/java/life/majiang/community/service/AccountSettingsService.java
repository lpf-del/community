package life.majiang.community.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import life.majiang.community.entity.AccountInformation;
import life.majiang.community.entity.UserEntity;
import life.majiang.community.mapper.UserEntityMapper;
import life.majiang.community.util.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lpf
 * @description 账号设置的服务层
 * @date 2021/9/20
 */
@Service
public class AccountSettingsService {

    @Resource
    private UserEntityMapper userEntityMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 账号安全要绑定的所有信息，要返回前端
     * @param userEntity
     * @return
     */
    public AccountInformation accountSafetyFactor(UserEntity userEntity){
        String qq = userEntity.getQq() != null ? userEntity.getQq() : "";
        String mail = userEntity.getMail() != null ? userEntity.getMail() : "";
        String telephone = userEntity.getTelephone() != null ? userEntity.getTelephone() : "";
        String weChat = userEntity.getWeChat() != null ? userEntity.getWeChat() : "";
        AccountInformation account = new AccountInformation();
        int accountSafetyFactor = 0;
        String accountSafes = "";
        if (qq.length() == 0){
            account.setQqTip("绑定qq账号");
            accountSafes += " 绑定qq账号";
        }else {
            accountSafetyFactor += 25;
            account.setQqTip("换绑qq账号");
        }
        if (mail.length() == 0){
            account.setMailTip("绑定邮箱");
            accountSafes += " 绑定邮箱";
        }else {
            account.setMailTip("换绑邮箱");
            accountSafetyFactor += 25;
        }
        if (telephone.length() == 0){
            account.setTelephoneTip("绑定手机");
            accountSafes += " 绑定手机";
        }else {
            accountSafetyFactor += 25;
            account.setTelephoneTip("换绑手机");
        }
        if (weChat.length() == 0){
            account.setWeChatTip("绑定微信");
            accountSafes += " 绑定微信";
        }else {
            account.setWeChatTip("换绑微信");
            accountSafetyFactor += 25;
        }
        account.setPassWordModification("修改密码");
        account.setAccountSafetyFactor(accountSafetyFactor);
        if (accountSafetyFactor < 50){
            account.setAccountSafe("高风险");
        }else if (accountSafetyFactor <= 75){
            account.setAccountSafe("低风险");
        }else {
            account.setAccountSafe("无风险");
        }
        if (accountSafes.length() == 0){
            accountSafes += "您的账号很安全";
        }else {
            accountSafes = "为了更好的保障您账号的安全，请您继续完善:" + accountSafes;
        }
        account.setAccountSafes(accountSafes);
        return account;
    }

    /**
     * 修改密码
     *      判断两次输入是否一致
     *      判断旧密码是否和数据库密码一致
     *      修改密码（md5）
     *
     * @param personInformation
     * @param oldPassword
     * @param newPassword
     * @param passwordConfirmation
     * @throws Exception
     */
    public void passwordModify(UserEntity personInformation, String oldPassword, String newPassword, String passwordConfirmation) throws Exception {
        if (!newPassword.equals(passwordConfirmation)){
            throw new Exception("两次密码不一致");
        }

        oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!personInformation.getPassWord().equals(oldPassword)){
            throw new Exception("旧密码输入不正确");
        }

        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("pass_word", newPassword);
        userEntityMapper.update(personInformation, updateWrapper);
    }

    /**
     *  旧手机号验证（解除旧手机，并非真正删除）
     *      验证手机号是否正确，验证码是否为真
     * @param oldTelephone
     * @param code
     * @param userEntity
     * @throws Exception
     */
    public void telephoneModifyCode(String oldTelephone, String code, UserEntity userEntity) throws Exception {
        if (!userEntity.getTelephone().equals(oldTelephone)){
            throw new Exception("手机号错误");
        }

        Object o = redisUtil.get(oldTelephone + "_code");
        if (o == null){
            throw new Exception("验证码失效");
        }

        String codeTel = o.toString();
        if (!code.equals(codeTel)){
            throw  new Exception("验证码输入错误");
        }
    }

    /**
     * 更新手机号，更新缓存
     * @param newTelephone
     * @param newcode
     * @param userEntity
     * @param response
     * @throws Exception
     */
    public void updatePhone(String newTelephone, String newcode,
                            UserEntity userEntity, HttpServletResponse response) throws Exception {
        Object o = redisUtil.get(newTelephone + "_code");
        if (o == null){
            throw new Exception("验证码失效");
        }

        if (!o.toString().equals(newcode)){
            throw new Exception("验证码输入错误");
        }

        /**
         * 更新电话
         */
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("telephone", newTelephone);
        userEntityMapper.update(userEntity, updateWrapper);

        /**
         * 将旧的user(传入的)数据更新手机号，放入缓存
         */
        userEntity.setTelephone(newTelephone);

        redisUtil.set(newTelephone, JSON.toJSONString(userEntity));
        response.addCookie(new Cookie("username", newTelephone));
    }

    /**
     * 检验邮箱是否正确， 验证码是否正确
     * @param oldemial
     * @param oldcodeemail
     * @param personInformation
     * @throws Exception
     */
    public void emailModifyCode(String oldemial, String oldcodeemail, UserEntity personInformation) throws Exception {
        System.out.println(oldemial+"_"+personInformation.getMail());
        if (!personInformation.getMail().equals(oldemial)){
            throw new Exception("邮箱输入错误");
        }

        Object o = redisUtil.get(oldemial + "_code");
        if (o == null){
            throw new Exception("验证码失效");
        }

        if (!o.toString().equals(oldcodeemail)){
            throw new Exception("验证码输入错误");
        }
    }

    /**
     * 更新邮箱，更新缓存
     * @param newemial
     * @param newcodemail
     * @param personInformation
     * @param response
     * @throws Exception
     */
    public void updateEamil(String newemial, String newcodemail, UserEntity personInformation, HttpServletResponse response) throws Exception {
        Object o = redisUtil.get(newemial + "_code");
        if (o == null){
            throw new Exception("验证码失效");
        }

        if (!o.toString().equals(newcodemail)){
            throw new Exception("验证码输入错误");
        }

        /**
         * 更新mysql
         */
        UpdateWrapper<UserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("mail", newemial);
        userEntityMapper.update(personInformation, updateWrapper);

        /**
         * 更新缓存
         */
        personInformation.setMail(newemial);
        redisUtil.set(newemial, JSON.toJSONString(personInformation));
        response.addCookie(new Cookie("username", newemial));
    }
}
