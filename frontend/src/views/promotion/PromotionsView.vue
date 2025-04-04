<script setup>
import { computed, onMounted, ref } from 'vue'
import Sidebar from '@/components/Sidebar.vue'
import Search from '@/components/Search.vue'
import Cart from '@/components/Cart.vue'
import router from '@/router'
import userApi from '@/api/userApi'
import promotionApi from '@/api/promotionApi'
import PromotionCard from '@/components/PromotionCard.vue'

const searchQuery = ref('')
const role = ref('')
const promotions = ref([])

// Fetch all promotions
const fetchPromotions = async () => {
    try {
        const { data: res } = await promotionApi.getAllPromotions();
        console.log("📢 API Response:", res); // ดูโครงสร้างของ res
        promotions.value = res.data || []; // ป้องกันกรณี res.data เป็น undefined
        console.log("✅ Promotions:", promotions.value);
    } catch (error) {
        console.error("❌ Failed to fetch promotions:", error);
    }
};

// Filter promotions based on the search query
const filteredPromotions = computed(() => {
    console.log("🔍 Filtered Promotions:", promotions.value);
    if (!searchQuery.value) return promotions.value;
    return promotions.value.filter((promotion) =>
        promotion.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
});


// Get user details and role
const getUser = async () => {
    try {
        const { data: res } = await userApi.getUserByJwt()
        role.value = res.data.role
    } catch (error) {
        console.error('Failed to fetch user role:', error)
    }
}

// Navigate to the add promotion page
const addPromotion = () => {
    router.push('/addpromotion')
}

// Fetch promotions and user details on mounted
onMounted(() => {
    fetchPromotions()
    getUser()
    console.log("Promotions Data:", promotions.value);
})

</script>

<template>
    <div class="flex">
        <!-- Sidebar -->
        <aside class="fixed">
            <Sidebar />
        </aside>

        <!-- Main Content -->
        <main
            class="ml-[14rem] w-full py-4 px-8 flex flex-col gap-4 bg-gray-50 min-h-screen h-full"
        >
            <!-- Search and Cart Section -->
            <section class="flex gap-4">
                <Search @update-search="searchQuery = $event" />
                <div class="flex items-center cursor-pointer">
                    <fa icon="bell" />
                </div>
                <Cart />
            </section>

            <!-- Name Section -->
            <section class="w-full h-12">
                <span class="font-bold text-3xl">Promotions</span>
            </section>

            <!-- Add Promotion Button (Visible to Admins only) -->
            <section>
                <div
                    v-if="role === 'ADMIN'"
                    @click="addPromotion"
                    class="inline py-2 px-4 rounded-md items-center cursor-pointer bg-yellow-300 shadow-md"
                >
                    <span><fa icon="plus" /> Add Promotion</span>
                </div>
            </section>

            <!-- Promotions List -->
            <section class="pb-12">
                <ul class="promotions-grid">
                    <li
                        v-for="promotion in filteredPromotions"
                        :key="promotion.id"
                    >
                        <PromotionCard :promotionData="promotion" />
                    </li>
                </ul>
                <p
                    v-if="filteredPromotions.length === 0"
                    class="mt-4 text-gray-500"
                >
                    No promotions available.
                </p>
            </section>
        </main>
    </div>
</template>