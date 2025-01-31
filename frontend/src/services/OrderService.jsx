import axiosInstance from "../constant/axiosInstance";
import { API_URL } from "../constant/project";

// 주문 추가
const postOrderLists = async (body) => {
  try {
    const response = await axiosInstance.post(`${API_URL}/order`, body);

    if (response) {
      return response;
    }
  } catch (error) {
    throw new Error("주문을 추가하는 중 오류가 발생했습니다.");
  }
};

const putOrderLists = async (body, id) => {
  try {
    const response = await axiosInstance.put(`${API_URL}/order/${id}`, body);

    if (response) {
      return response;
    }
  } catch (error) {
    throw new Error("주문을 수정하는 중 오류가 발생했습니다.");
  }
};

const deleteOrderLists = async (id) => {
  try {
    const response = await axiosInstance.delete(`${API_URL}/order/${id}`);

    if (response) {
      return response;
    }
  } catch (error) {
    throw new Error("주문을 수정하는 중 오류가 발생했습니다.");
  }
};

const getTodayOrderLists = async () => {
  try {
    const response = await axiosInstance.get(`${API_URL}/order/today-delivery`);

    if (response) {
      return response?.data;
    }
  } catch (error) {
    throw new Error("주문을 불러오는 중 오류가 발생했습니다.");
  }
};

const getTommorowOrderLists = async () => {
  try {
    const response = await axiosInstance.get(
      `${API_URL}/order/tomorrow-delivery`
    );

    if (response) {
      return response?.data;
    }
  } catch (error) {
    throw new Error("주문을 불러오는 중 오류가 발생했습니다.");
  }
};

const OrderService = {
  postOrderLists,
  putOrderLists,
  deleteOrderLists,
  getTodayOrderLists,
  getTommorowOrderLists,
};

export { OrderService };
