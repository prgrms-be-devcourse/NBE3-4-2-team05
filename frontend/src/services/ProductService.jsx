import axiosInstance from "../constant/axiosInstance";
import { API_URL } from "../constant/project";

// 상품 리스트
const getProductLists = async () => {
  try {
    const response = await axiosInstance.get(`${API_URL}/product`);
    if (response?.data) {
      return response.data;
    }
  } catch (error) {}
};

// 상품 추가
const postProductLists = async () => {
  const randomPrice = Math.floor(Math.random() * (50000 - 10000 + 1)) + 10000;

  const name = `테스트 상품`;

  // body 생성
  const body = {
    type: "커피콩",
    name: name,
    imageUrl: "https://i.imgur.com/HKOFQYa.jpeg",
    content: `${name} 설명글입니다.`,
    price: randomPrice,
  };

  try {
    const response = await axiosInstance.post(`${API_URL}/admin/product`, body);
    const res = response;
    if (res) {
      return res;
    }
    return {};
  } catch (error) {
    throw new Error("상품을 추가하는 중 오류가 발생했습니다.");
  }
};

const ProductService = {
  getProductLists,
  postProductLists,
};

export { ProductService };
